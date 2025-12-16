package dal;

import model.OrderIntent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderIntentDao extends DBContext {

    public int createOrderIntent(OrderIntent intent) {
        if (connection == null) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return -1;
        }
        
        String sql = "INSERT INTO order_intents (user_id, job_id, status, cart_items, coupon_code, subtotal_amount, discount_amount, total_amount, created_at, expires_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 10 MINUTE))";
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, intent.getUserId());
            ps.setInt(2, intent.getJobId());
            ps.setString(3, intent.getStatus());
            ps.setString(4, intent.getCartItems());
            ps.setString(5, intent.getCouponCode());
            ps.setDouble(6, intent.getSubtotalAmount());
            ps.setDouble(7, intent.getDiscountAmount());
            ps.setDouble(8, intent.getTotalAmount());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Error creating order intent", ex);
        }
        return -1;
    }

    public OrderIntent getIntentById(int intentId) {
        if (connection == null) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return null;
        }
        
        String sql = "SELECT * FROM order_intents WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, intentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToIntent(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Error getting order intent by id", ex);
        }
        return null;
    }

    public OrderIntent getIntentByJobId(int jobId) {
        if (connection == null) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return null;
        }
        
        String sql = "SELECT * FROM order_intents WHERE job_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToIntent(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Error getting order intent by job id", ex);
        }
        return null;
    }

    public void updateStatus(int intentId, String status, Integer orderId, String errorMessage) {
        if (connection == null) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return;
        }
        
        String sql = "UPDATE order_intents SET status = ?, order_id = ?, error_message = ?, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            if (orderId != null) {
                ps.setInt(2, orderId);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, errorMessage);
            ps.setInt(4, intentId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderIntentDao.class.getName()).log(Level.SEVERE, "Error updating order intent status", ex);
        }
    }

    private OrderIntent mapResultSetToIntent(ResultSet rs) throws SQLException {
        OrderIntent intent = new OrderIntent();
        intent.setId(rs.getInt("id"));
        intent.setUserId(rs.getInt("user_id"));
        intent.setJobId(rs.getInt("job_id"));
        
        Integer orderId = rs.getInt("order_id");
        if (rs.wasNull()) {
            intent.setOrderId(null);
        } else {
            intent.setOrderId(orderId);
        }
        
        intent.setStatus(rs.getString("status"));
        intent.setCartItems(rs.getString("cart_items"));
        intent.setCouponCode(rs.getString("coupon_code"));
        intent.setSubtotalAmount(rs.getDouble("subtotal_amount"));
        intent.setDiscountAmount(rs.getDouble("discount_amount"));
        intent.setTotalAmount(rs.getDouble("total_amount"));
        intent.setErrorMessage(rs.getString("error_message"));
        intent.setCreatedAt(rs.getTimestamp("created_at"));
        intent.setUpdatedAt(rs.getTimestamp("updated_at"));
        intent.setExpiresAt(rs.getTimestamp("expires_at"));
        return intent;
    }
}
