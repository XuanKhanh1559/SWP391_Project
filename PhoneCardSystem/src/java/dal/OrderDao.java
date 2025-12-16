package dal;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDao extends DBContext {
    private ProductDao productDao;
    private CouponDao couponDao;

    public OrderDao() {
        super();
        this.productDao = new ProductDao();
        this.couponDao = new CouponDao();
    }

    public int createOrderFromJob(CreateOrderPayload payload) throws Exception {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/phone_card_system", "root", "123456");
            conn.setAutoCommit(false);

            String lockUserSql = "SELECT id, balance FROM users WHERE id = ? FOR UPDATE";
            PreparedStatement lockUserPs = conn.prepareStatement(lockUserSql);
            lockUserPs.setInt(1, payload.getUserId());
            ResultSet userRs = lockUserPs.executeQuery();
            
            double userBalance = 0;
            if (userRs.next()) {
                userBalance = userRs.getDouble("balance");
            } else {
                throw new Exception("Người dùng không tồn tại");
            }
            
            if (userBalance < payload.getTotalAmount()) {
                throw new Exception("Số dư không đủ để thanh toán");
            }

            Coupon coupon = null;
            if (payload.getCouponCode() != null && !payload.getCouponCode().trim().isEmpty()) {
                coupon = couponDao.getCouponByCode(payload.getCouponCode());
                if (coupon == null) {
                    throw new Exception("Mã giảm giá không tồn tại");
                }

                if (!couponDao.canUserUseCouponFromUserCoupons(payload.getUserId(), coupon.getId())) {
                    throw new Exception("Bạn không sở hữu mã này hoặc đã sử dụng hết lượt dùng");
                }
            }

            List<ProductStorage> selectedStorages = new ArrayList<>();
            for (CreateOrderPayload.CartItemData item : payload.getCartItems()) {
                Product product = productDao.getProductById(item.getProductId(), false);
                if (product == null) {
                    throw new Exception("Sản phẩm không tồn tại");
                }

                String selectStorageSql = "SELECT id, product_id, card_code, card_serial, status FROM product_storage WHERE product_id = ? AND status = 'available' AND deleted = 0 ORDER BY created_at ASC LIMIT ? FOR UPDATE";
                PreparedStatement ps = conn.prepareStatement(selectStorageSql);
                ps.setInt(1, item.getProductId());
                ps.setInt(2, item.getQuantity());
                ResultSet rs = ps.executeQuery();

                int count = 0;
                while (rs.next()) {
                    ProductStorage storage = new ProductStorage();
                    storage.setId(rs.getInt("id"));
                    storage.setProduct_id(rs.getInt("product_id"));
                    storage.setCard_code(rs.getString("card_code"));
                    storage.setCard_serial(rs.getString("card_serial"));
                    storage.setStatus(rs.getString("status"));
                    selectedStorages.add(storage);
                    count++;
                }

                if (count < item.getQuantity()) {
                    throw new Exception("Sản phẩm " + product.getName() + " không đủ số lượng (còn: " + count + ", cần: " + item.getQuantity() + ")");
                }
            }

            String transactionCode = "TXN" + System.currentTimeMillis();
            String paymentSql = "INSERT INTO payment_transactions (user_id, transaction_code, type, amount, balance_before, balance_after, status, description) VALUES (?, ?, 'purchase', ?, ?, ?, 'completed', ?)";
            int paymentTransactionId;
            PreparedStatement paymentPs = conn.prepareStatement(paymentSql, Statement.RETURN_GENERATED_KEYS);
            paymentPs.setInt(1, payload.getUserId());
            paymentPs.setString(2, transactionCode);
            paymentPs.setDouble(3, payload.getTotalAmount());
            paymentPs.setDouble(4, userBalance);
            paymentPs.setDouble(5, userBalance - payload.getTotalAmount());
            paymentPs.setString(6, "Thanh toán đơn hàng");
            paymentPs.executeUpdate();
            
            ResultSet paymentRs = paymentPs.getGeneratedKeys();
            if (paymentRs.next()) {
                paymentTransactionId = paymentRs.getInt(1);
            } else {
                throw new Exception("Không thể tạo payment transaction");
            }

            String updateBalanceSql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement updateBalancePs = conn.prepareStatement(updateBalanceSql);
            updateBalancePs.setDouble(1, payload.getTotalAmount());
            updateBalancePs.setInt(2, payload.getUserId());
            updateBalancePs.executeUpdate();

            String orderCode = "ORD" + System.currentTimeMillis();
            String orderSql = "INSERT INTO orders (user_id, payment_transaction_id, order_code, subtotal_amount, discount_amount, total_amount, final_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'pending')";
            int orderId;
            PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderPs.setInt(1, payload.getUserId());
            orderPs.setInt(2, paymentTransactionId);
            orderPs.setString(3, orderCode);
            orderPs.setDouble(4, payload.getSubtotalAmount());
            orderPs.setDouble(5, payload.getDiscountAmount());
            orderPs.setDouble(6, payload.getSubtotalAmount() - payload.getDiscountAmount());
            orderPs.setDouble(7, payload.getTotalAmount());
            orderPs.executeUpdate();

            ResultSet orderRs = orderPs.getGeneratedKeys();
            if (orderRs.next()) {
                orderId = orderRs.getInt(1);
            } else {
                throw new Exception("Không thể tạo order");
            }

            String orderItemSql = "INSERT INTO order_items (order_id, product_id, product_name_snapshot, product_storage_id, quantity, unit_price, price_at_purchase, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement itemPs = conn.prepareStatement(orderItemSql);
            
            int storageIndex = 0;
            for (CreateOrderPayload.CartItemData item : payload.getCartItems()) {
                Product product = productDao.getProductById(item.getProductId(), false);
                double productPrice = product.getPrice();
                
                for (int i = 0; i < item.getQuantity(); i++) {
                    ProductStorage storage = selectedStorages.get(storageIndex++);
                    itemPs.setInt(1, orderId);
                    itemPs.setInt(2, product.getId());
                    itemPs.setString(3, product.getName());
                    itemPs.setInt(4, storage.getId());
                    itemPs.setInt(5, 1);
                    itemPs.setDouble(6, productPrice);
                    itemPs.setDouble(7, productPrice);
                    itemPs.setDouble(8, productPrice);
                    itemPs.addBatch();
                }
            }
            itemPs.executeBatch();

            String updateStorageSql = "UPDATE product_storage SET status = 'sold', updated_at = NOW() WHERE id = ?";
            PreparedStatement storagePs = conn.prepareStatement(updateStorageSql);
            for (ProductStorage storage : selectedStorages) {
                storagePs.setInt(1, storage.getId());
                storagePs.addBatch();
            }
            storagePs.executeBatch();

            if (coupon != null) {
                String couponUsageSql = "INSERT INTO coupon_usages (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
                PreparedStatement couponUsagePs = conn.prepareStatement(couponUsageSql);
                couponUsagePs.setInt(1, coupon.getId());
                couponUsagePs.setInt(2, payload.getUserId());
                couponUsagePs.setInt(3, orderId);
                couponUsagePs.executeUpdate();

                String orderCouponSql = "INSERT INTO order_coupons (order_id, coupon_code, coupon_name, discount_type, discount_value, min_order_amount, max_discount_amount, discount_applied) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement orderCouponPs = conn.prepareStatement(orderCouponSql);
                orderCouponPs.setInt(1, orderId);
                orderCouponPs.setString(2, coupon.getCode());
                orderCouponPs.setString(3, coupon.getName());
                String discountTypeLabel = coupon.getDiscount_type() == 1 ? "percentage" : "fixed_amount";
                orderCouponPs.setString(4, discountTypeLabel);
                orderCouponPs.setDouble(5, coupon.getDiscount_value());
                orderCouponPs.setDouble(6, coupon.getMin_order_amount());
                if (coupon.getMax_discount_amount() != null) {
                    orderCouponPs.setDouble(7, coupon.getMax_discount_amount());
                } else {
                    orderCouponPs.setNull(7, Types.DOUBLE);
                }
                orderCouponPs.setDouble(8, payload.getDiscountAmount());
                orderCouponPs.executeUpdate();
            }

            String clearCartSql = "DELETE FROM cart_items WHERE user_id = ?";
            PreparedStatement clearCartPs = conn.prepareStatement(clearCartSql);
            clearCartPs.setInt(1, payload.getUserId());
            clearCartPs.executeUpdate();
            
            String updateOrderStatusSql = "UPDATE orders SET status = 'completed', updated_at = NOW() WHERE id = ?";
            PreparedStatement updateOrderPs = conn.prepareStatement(updateOrderStatusSql);
            updateOrderPs.setInt(1, orderId);
            updateOrderPs.executeUpdate();

            conn.commit();
            return orderId;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "Error rolling back transaction", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "Error closing connection", e);
                }
            }
        }
    }

    public List<Order> getOrdersByUserId(int userId, int limit, int offset) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? AND deleted = 0 ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "Error getting orders by user id", e);
        }
        
        return orders;
    }

    public int countOrdersByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ? AND deleted = 0";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "Error counting orders by user id", e);
        }
        
        return 0;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND deleted = 0";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "Error getting order by id", e);
        }
        
        return null;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUser_id(rs.getInt("user_id"));
        order.setPayment_transaction_id(rs.getInt("payment_transaction_id"));
        
        Integer couponId = (Integer) rs.getObject("coupon_id");
        order.setCoupon_id(couponId);
        
        order.setOrder_code(rs.getString("order_code"));
        order.setSubtotal_amount(rs.getDouble("subtotal_amount"));
        order.setDiscount_amount(rs.getDouble("discount_amount"));
        order.setTotal_amount(rs.getDouble("total_amount"));
        
        String statusStr = rs.getString("status");
        if ("pending".equals(statusStr)) {
            order.setStatus(0);
        } else if ("completed".equals(statusStr)) {
            order.setStatus(1);
        } else if ("cancelled".equals(statusStr)) {
            order.setStatus(2);
        } else {
            order.setStatus(0);
        }
        
        order.setNotes(rs.getString("notes"));
        order.setCreated_at(rs.getTimestamp("created_at"));
        order.setUpdated_at(rs.getTimestamp("updated_at"));
        order.setDeleted(rs.getInt("deleted"));
        
        return order;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ? AND deleted = 0 ORDER BY id ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToOrderItem(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "Error getting order items", e);
        }
        
        return items;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrder_id(rs.getInt("order_id"));
        item.setProduct_id(rs.getInt("product_id"));
        item.setProduct_name_snapshot(rs.getString("product_name_snapshot"));
        
        Integer productStorageId = (Integer) rs.getObject("product_storage_id");
        item.setProduct_storage_id(productStorageId);
        
        item.setQuantity(rs.getInt("quantity"));
        item.setUnit_price(rs.getDouble("unit_price"));
        item.setTotal_price(rs.getDouble("total_price"));
        item.setCreated_at(rs.getTimestamp("created_at"));
        item.setUpdated_at(rs.getTimestamp("updated_at"));
        item.setDeleted(rs.getInt("deleted"));
        
        return item;
    }
}
