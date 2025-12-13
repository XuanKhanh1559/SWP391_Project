package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsDao extends DBContext {

    public int getTotalUsers() {
        if (connection == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM users WHERE deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getTotalActiveUsers() {
        if (connection == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM users WHERE status = 'active' AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getTotalProducts() {
        if (connection == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM products WHERE deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getTotalActiveProducts() {
        if (connection == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM products WHERE status = 'active' AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getTotalOrders() {
        if (connection == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM orders WHERE deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public double getTotalRevenue() {
        if (connection == null) {
            return 0.0;
        }
        String sql = "SELECT SUM(total_amount) FROM orders WHERE status = 'completed' AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public int getTotalPendingOrders() {
        if (connection == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM orders WHERE status = 'pending' AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getTotalCompletedOrders() {
        if (connection == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM orders WHERE status = 'completed' AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public double getTotalUserBalance() {
        if (connection == null) {
            return 0.0;
        }
        String sql = "SELECT SUM(balance) FROM users WHERE deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }
}

