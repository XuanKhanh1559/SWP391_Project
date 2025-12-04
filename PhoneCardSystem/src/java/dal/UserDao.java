package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.User;
import config.UserStatus;
import util.PasswordUtil;

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
        String sql = "SELECT id, username, email, password, phone, balance, status, created_at, updated_at, deleted "
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
        String sql = "SELECT id, username, email, password, phone, balance, status, created_at, updated_at, deleted "
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
        String sql = "INSERT INTO users (username, email, password, phone, balance, status, created_at, updated_at, deleted) "
                   + "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW(), 0)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setDouble(5, user.getBalance());
            ps.setString(6, UserStatus.ACTIVE.getLabel());
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
}
