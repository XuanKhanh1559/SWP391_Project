package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.User;
import config.UserStatus;
import util.PasswordUtil;
import util.MailUtil;

public class UserDao extends DBContext {

    private String lastErrorMessage;

    public String getLastError() {
        return lastErrorMessage;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setBalance(rs.getDouble("balance"));
        String statusStr = rs.getString("status");
        UserStatus status = UserStatus.fromLabel(statusStr);
        if (status != null) {
            user.setStatus(status.getValue());
        } else {
            user.setStatus(UserStatus.ACTIVE.getValue());
        }
        String roleStr = rs.getString("role");
        user.setRole(roleStr != null ? roleStr : "user");
        user.setCreated_at(rs.getTimestamp("created_at"));
        user.setUpdated_at(rs.getTimestamp("updated_at"));
        user.setDeleted(rs.getInt("deleted"));
        return user;
    }

    public User signin(String email, String password) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return null;
        }
        String sql = "SELECT id, username, email, password, phone, balance, status, role, created_at, updated_at, deleted "
                   + "FROM users WHERE email = ? AND status = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, UserStatus.ACTIVE.getLabel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (PasswordUtil.verifyPassword(password, hashedPassword)) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi đăng nhập: " + ex.getMessage();
        }
        return null;
    }

    public User signinByUsername(String username, String password) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return null;
        }
        String sql = "SELECT id, username, email, password, phone, balance, status, role, created_at, updated_at, deleted "
                   + "FROM users WHERE username = ? AND status = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, UserStatus.ACTIVE.getLabel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (PasswordUtil.verifyPassword(password, hashedPassword)) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi đăng nhập: " + ex.getMessage();
        }
        return null;
    }

    public boolean checkEmailExists(String email) {
        if (connection == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checkUsernameExists(String username) {
        if (connection == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public int signup(User user) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return -1;
        }
        String sql = "INSERT INTO users (username, email, password, phone, balance, status, role, created_at, updated_at, deleted) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), 0)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setDouble(5, user.getBalance());
            ps.setString(6, UserStatus.ACTIVE.getLabel());
            String role = user.getRole();
            ps.setString(7, role != null && !role.isEmpty() ? role : "user");
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi đăng ký: " + ex.getMessage();
        }
        return -1;
    }

    public User getUserById(int userId) {
        if (connection == null) {
            return null;
        }
        String sql = "SELECT id, username, email, password, phone, balance, status, role, created_at, updated_at, deleted "
                   + "FROM users WHERE id = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        if (connection == null) {
            return null;
        }
        String sql = "SELECT id, username, email, password, phone, balance, status, role, created_at, updated_at, deleted "
                   + "FROM users WHERE email = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean updateProfile(int userId, String email, String phone) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }
        String sql = "UPDATE users SET email = ?, phone = ?, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, phone);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi cập nhật thông tin: " + ex.getMessage();
        }
        return false;
    }

    public boolean verifyCurrentPassword(int userId, String password) {
        if (connection == null) {
            return false;
        }
        String sql = "SELECT password FROM users WHERE id = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return PasswordUtil.verifyPassword(password, hashedPassword);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updatePasswordByUserId(int userId, String newPassword) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        String sql = "UPDATE users SET password = ?, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi cập nhật mật khẩu: " + ex.getMessage();
        }
        return false;
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        String sql = "UPDATE users SET password = ?, updated_at = NOW() WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, hashedPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi cập nhật mật khẩu: " + ex.getMessage();
        }
        return false;
    }

    public boolean requestPasswordReset(String email) {
        lastErrorMessage = null;
        User user = getUserByEmail(email);
        if (user == null) {
            lastErrorMessage = "Email không tồn tại trong hệ thống";
            return false;
        }
        
        String token = String.format("%06d", (int)(Math.random() * 1_000_000));
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000);
        
        boolean stored = storePasswordResetToken(user.getId(), token, expiresAt);
        if (!stored) {
            lastErrorMessage = "Không thể lưu mã đặt lại mật khẩu";
            return false;
        }
        
        boolean sent = MailUtil.sendVerificationEmail(email, token);
        if (!sent) {
            lastErrorMessage = "Gửi email thất bại. Vui lòng kiểm tra cấu hình SMTP";
            return false;
        }
        
        return true;
    }

    public boolean storePasswordResetToken(int userId, String token, Timestamp expiresAt) {
        if (connection == null) {
            return false;
        }
        String sql = "INSERT INTO password_resets (user_id, token, expires_at, created_at) VALUES (?, ?, ?, NOW())";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expiresAt);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean verifyPasswordResetToken(String email, String token) {
        if (connection == null) {
            return false;
        }
        String sql = "SELECT pr.id FROM password_resets pr "
                   + "INNER JOIN users u ON pr.user_id = u.id "
                   + "WHERE u.email = ? AND pr.token = ? AND pr.expires_at > NOW() AND pr.used = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, token);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean markTokenAsUsed(String email, String token) {
        if (connection == null) {
            return false;
        }
        String sql = "UPDATE password_resets pr "
                   + "INNER JOIN users u ON pr.user_id = u.id "
                   + "SET pr.used = 1 WHERE u.email = ? AND pr.token = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, token);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public List<User> getAllUsers(String search, String status, int page, int pageSize) {
        List<User> users = new ArrayList<>();
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return users;
        }

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, username, email, password, phone, balance, status, role, created_at, updated_at, deleted ");
        sql.append("FROM users WHERE 1=1 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (username LIKE ? OR email LIKE ?) ");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND status = ? ");
            params.add(status);
        }

        sql.append("ORDER BY created_at DESC ");
        sql.append("LIMIT ? OFFSET ?");

        int offset = (page - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi lấy danh sách người dùng: " + ex.getMessage();
        }
        return users;
    }

    public int countUsers(String search, String status) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return 0;
        }

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) as total FROM users WHERE 1=1 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (username LIKE ? OR email LIKE ?) ");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND status = ? ");
            params.add(status);
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi đếm số lượng người dùng: " + ex.getMessage();
        }
        return 0;
    }

    public boolean banUser(int userId) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        String sql = "UPDATE users SET status = ?, updated_at = NOW() WHERE id = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, UserStatus.BANNED.getLabel());
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi khóa người dùng: " + ex.getMessage();
        }
        return false;
    }

    public boolean unbanUser(int userId) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        String sql = "UPDATE users SET status = ?, updated_at = NOW() WHERE id = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, UserStatus.ACTIVE.getLabel());
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi mở khóa người dùng: " + ex.getMessage();
        }
        return false;
    }

    public boolean deposit(int userId, double amount, String description) {
        if (amount <= 0) {
            lastErrorMessage = "Số tiền nạp phải lớn hơn 0";
            return false;
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/phone_card_system", "root", "123456");
            conn.setAutoCommit(false);

            String lockUserSql = "SELECT id, balance FROM users WHERE id = ? FOR UPDATE";
            PreparedStatement lockUserPs = conn.prepareStatement(lockUserSql);
            lockUserPs.setInt(1, userId);
            ResultSet userRs = lockUserPs.executeQuery();
            
            double currentBalance = 0;
            if (userRs.next()) {
                currentBalance = userRs.getDouble("balance");
            } else {
                throw new SQLException("Người dùng không tồn tại");
            }

            String transactionCode = "DEP" + System.currentTimeMillis();
            String paymentSql = "INSERT INTO payment_transactions (user_id, transaction_code, type, amount, balance_before, balance_after, status, description) VALUES (?, ?, 'deposit', ?, ?, ?, 'completed', ?)";
            PreparedStatement paymentPs = conn.prepareStatement(paymentSql);
            paymentPs.setInt(1, userId);
            paymentPs.setString(2, transactionCode);
            paymentPs.setDouble(3, amount);
            paymentPs.setDouble(4, currentBalance);
            paymentPs.setDouble(5, currentBalance + amount);
            paymentPs.setString(6, description != null ? description : "Nạp tiền");
            paymentPs.executeUpdate();

            String updateBalanceSql = "UPDATE users SET balance = balance + ?, updated_at = NOW() WHERE id = ?";
            PreparedStatement updateBalancePs = conn.prepareStatement(updateBalanceSql);
            updateBalancePs.setDouble(1, amount);
            updateBalancePs.setInt(2, userId);
            updateBalancePs.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi nạp tiền: " + ex.getMessage();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
