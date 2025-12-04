package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;
import config.UserStatus;

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
                   + "FROM users WHERE email = ? AND password = ? AND status = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, UserStatus.ACTIVE.getLabel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
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
                   + "FROM users WHERE username = ? AND password = ? AND status = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, UserStatus.ACTIVE.getLabel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi đăng nhập: " + ex.getMessage();
        }
        return null;
    }
}
