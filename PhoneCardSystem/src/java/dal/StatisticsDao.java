package dal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsDao extends DBContext {

    public JsonObject getRevenueStatistics(String period, String year, String month) {
        JsonObject result = new JsonObject();
        JsonArray labels = new JsonArray();
        JsonArray data = new JsonArray();
        double total = 0;
        
        if (connection == null) {
            result.addProperty("error", "Database connection failed");
            return result;
        }
        
        String sql = "";
        PreparedStatement ps = null;
        
        try {
            if ("day".equals(period)) {
                // Doanh thu theo ngày trong tháng
                sql = "SELECT DAY(created_at) as period, SUM(total_amount) as revenue " +
                      "FROM orders " +
                      "WHERE status = 'completed' AND deleted = 0 " +
                      "AND YEAR(created_at) = ? AND MONTH(created_at) = ? " +
                      "GROUP BY DAY(created_at) " +
                      "ORDER BY DAY(created_at)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, year);
                ps.setString(2, month);
                
            } else if ("week".equals(period)) {
                // Doanh thu theo tuần trong 3 tháng
                sql = "SELECT CONCAT('Tuần ', WEEK(created_at, 1) - WEEK(DATE_SUB(created_at, INTERVAL DAYOFMONTH(created_at) - 1 DAY), 1) + 1, '/', MONTH(created_at)) as period, " +
                      "SUM(total_amount) as revenue " +
                      "FROM orders " +
                      "WHERE status = 'completed' AND deleted = 0 " +
                      "AND created_at >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
                      "GROUP BY YEAR(created_at), MONTH(created_at), WEEK(created_at, 1) " +
                      "ORDER BY YEAR(created_at), MONTH(created_at), WEEK(created_at, 1)";
                ps = connection.prepareStatement(sql);
                
            } else if ("month".equals(period)) {
                // Doanh thu theo tháng trong năm
                sql = "SELECT MONTH(created_at) as period, SUM(total_amount) as revenue " +
                      "FROM orders " +
                      "WHERE status = 'completed' AND deleted = 0 " +
                      "AND YEAR(created_at) = ? " +
                      "GROUP BY MONTH(created_at) " +
                      "ORDER BY MONTH(created_at)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, year);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String periodLabel = rs.getString("period");
                double revenue = rs.getDouble("revenue");
                
                if ("day".equals(period)) {
                    labels.add(new JsonPrimitive("Ngày " + periodLabel));
                } else if ("month".equals(period)) {
                    labels.add(new JsonPrimitive("Tháng " + periodLabel));
                } else {
                    labels.add(new JsonPrimitive(periodLabel));
                }
                
                data.add(new JsonPrimitive(revenue));
                total += revenue;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, "Error getting revenue statistics", ex);
            result.addProperty("error", ex.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        result.add("labels", labels);
        result.add("data", data);
        result.addProperty("total", total);
        
        return result;
    }

    public JsonObject getUserStatistics(String period, String year, String month) {
        JsonObject result = new JsonObject();
        JsonArray labels = new JsonArray();
        JsonArray data = new JsonArray();
        int total = 0;
        
        if (connection == null) {
            result.addProperty("error", "Database connection failed");
            return result;
        }
        
        String sql = "";
        PreparedStatement ps = null;
        
        try {
            if ("day".equals(period)) {
                // Người dùng mới theo ngày trong tháng
                sql = "SELECT DAY(created_at) as period, COUNT(*) as count " +
                      "FROM users " +
                      "WHERE deleted = 0 AND role = 'user' " +
                      "AND YEAR(created_at) = ? AND MONTH(created_at) = ? " +
                      "GROUP BY DAY(created_at) " +
                      "ORDER BY DAY(created_at)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, year);
                ps.setString(2, month);
                
            } else if ("week".equals(period)) {
                // Người dùng mới theo tuần trong 3 tháng
                sql = "SELECT CONCAT('Tuần ', WEEK(created_at, 1) - WEEK(DATE_SUB(created_at, INTERVAL DAYOFMONTH(created_at) - 1 DAY), 1) + 1, '/', MONTH(created_at)) as period, " +
                      "COUNT(*) as count " +
                      "FROM users " +
                      "WHERE deleted = 0 AND role = 'user' " +
                      "AND created_at >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
                      "GROUP BY YEAR(created_at), MONTH(created_at), WEEK(created_at, 1) " +
                      "ORDER BY YEAR(created_at), MONTH(created_at), WEEK(created_at, 1)";
                ps = connection.prepareStatement(sql);
                
            } else if ("month".equals(period)) {
                // Người dùng mới theo tháng trong năm
                sql = "SELECT MONTH(created_at) as period, COUNT(*) as count " +
                      "FROM users " +
                      "WHERE deleted = 0 AND role = 'user' " +
                      "AND YEAR(created_at) = ? " +
                      "GROUP BY MONTH(created_at) " +
                      "ORDER BY MONTH(created_at)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, year);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String periodLabel = rs.getString("period");
                int count = rs.getInt("count");
                
                if ("day".equals(period)) {
                    labels.add(new JsonPrimitive("Ngày " + periodLabel));
                } else if ("month".equals(period)) {
                    labels.add(new JsonPrimitive("Tháng " + periodLabel));
                } else {
                    labels.add(new JsonPrimitive(periodLabel));
                }
                
                data.add(new JsonPrimitive(count));
                total += count;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, "Error getting user statistics", ex);
            result.addProperty("error", ex.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        result.add("labels", labels);
        result.add("data", data);
        result.addProperty("total", total);
        
        return result;
    }

    public JsonObject getTotalStatistics() {
        JsonObject result = new JsonObject();
        
        if (connection == null) {
            result.addProperty("error", "Database connection failed");
            return result;
        }
        
        try {
            // Tổng doanh thu
            String revenueSql = "SELECT SUM(total_amount) as total FROM orders WHERE status = 'completed' AND deleted = 0";
            PreparedStatement ps1 = connection.prepareStatement(revenueSql);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                result.addProperty("totalRevenue", rs1.getDouble("total"));
            }
            
            // Tổng số người dùng
            String userSql = "SELECT COUNT(*) as total FROM users WHERE deleted = 0 AND role = 'user'";
            PreparedStatement ps2 = connection.prepareStatement(userSql);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                result.addProperty("totalUsers", rs2.getInt("total"));
            }
            
            // Tổng số đơn hàng
            String orderSql = "SELECT COUNT(*) as total FROM orders WHERE deleted = 0";
            PreparedStatement ps3 = connection.prepareStatement(orderSql);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                result.addProperty("totalOrders", rs3.getInt("total"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, "Error getting total statistics", ex);
            result.addProperty("error", ex.getMessage());
        }
        
        return result;
    }

    public int getTotalUsers() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT COUNT(*) as total FROM users WHERE deleted = 0 AND role = 'user'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getTotalActiveUsers() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT COUNT(*) as total FROM users WHERE deleted = 0 AND role = 'user' AND status = 'active'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getTotalProducts() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT COUNT(*) as total FROM products WHERE deleted = 0";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getTotalActiveProducts() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT COUNT(*) as total FROM products WHERE deleted = 0 AND (status = 'active' OR status = 1)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getTotalOrders() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT COUNT(*) as total FROM orders WHERE deleted = 0";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getTotalPendingOrders() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT COUNT(*) as total FROM orders WHERE deleted = 0 AND status = 'pending'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getTotalCompletedOrders() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT COUNT(*) as total FROM orders WHERE deleted = 0 AND status = 'completed'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getTotalRevenue() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT SUM(total_amount) as total FROM orders WHERE deleted = 0 AND status = 'completed'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getTotalUserBalance() {
        if (connection == null) return 0;
        try {
            String sql = "SELECT SUM(balance) as total FROM users WHERE deleted = 0 AND role = 'user'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
