package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Coupon;
import config.CouponStatus;
import config.DiscountType;

public class CouponDao extends DBContext {

    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getInt("id"));
        coupon.setCampaign_id((Integer) rs.getObject("campaign_id"));
        coupon.setCode(rs.getString("code"));
        coupon.setName(rs.getString("name"));
        coupon.setDescription(rs.getString("description"));
        
        Object discountTypeObj = rs.getObject("discount_type");
        if (discountTypeObj instanceof String) {
            DiscountType discountType = DiscountType.fromLabel((String) discountTypeObj);
            if (discountType != null) {
                coupon.setDiscount_type(discountType.getValue());
            } else {
                coupon.setDiscount_type(1);
            }
        } else if (discountTypeObj instanceof Number) {
            coupon.setDiscount_type(((Number) discountTypeObj).intValue());
        } else {
            coupon.setDiscount_type(1);
        }
        
        Object discountValueObj = rs.getObject("discount_value");
        if (discountValueObj instanceof Number) {
            coupon.setDiscount_value(((Number) discountValueObj).doubleValue());
        } else {
            coupon.setDiscount_value(0.0);
        }
        
        Object minOrderAmountObj = rs.getObject("min_order_amount");
        if (minOrderAmountObj instanceof Number) {
            coupon.setMin_order_amount(((Number) minOrderAmountObj).doubleValue());
        } else {
            coupon.setMin_order_amount(0.0);
        }
        
        Object maxDiscountAmountObj = rs.getObject("max_discount_amount");
        if (maxDiscountAmountObj != null && maxDiscountAmountObj instanceof Number) {
            coupon.setMax_discount_amount(((Number) maxDiscountAmountObj).doubleValue());
        } else {
            coupon.setMax_discount_amount(null);
        }
        
        coupon.setStart_date(rs.getTimestamp("start_date"));
        coupon.setEnd_date(rs.getTimestamp("end_date"));
        coupon.setUsage_limit_per_user(rs.getInt("usage_limit_per_user"));
        coupon.setTotal_usage_limit((Integer) rs.getObject("total_usage_limit"));
        coupon.setCurrent_usage_count(rs.getInt("current_usage_count"));
        
        Object statusObj = rs.getObject("status");
        if (statusObj instanceof String) {
            CouponStatus status = CouponStatus.fromLabel((String) statusObj);
            if (status != null) {
                coupon.setStatus(status.getValue());
            } else {
                coupon.setStatus(1);
            }
        } else if (statusObj instanceof Number) {
            coupon.setStatus(((Number) statusObj).intValue());
        } else {
            coupon.setStatus(1);
        }
        
        coupon.setApplicable_product_ids(rs.getString("applicable_product_ids"));
        coupon.setApplicable_provider_ids(rs.getString("applicable_provider_ids"));
        coupon.setCreated_at(rs.getTimestamp("created_at"));
        coupon.setUpdated_at(rs.getTimestamp("updated_at"));
        coupon.setDeleted(rs.getInt("deleted"));
        return coupon;
    }

    public List<Coupon> getAllCoupons(int limit, int offset, String search, Integer status) {
        List<Coupon> coupons = new ArrayList<>();
        if (connection == null) {
            return coupons;
        }

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM coupons WHERE deleted = 0"
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (code LIKE ? OR name LIKE ? OR description LIKE ?)");
        }

        if (status != null) {
            sql.append(" AND status = ?");
        }

        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int paramIndex = 1;

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }

            if (status != null) {
                ps.setInt(paramIndex++, status);
            }

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return coupons;
    }

    public int countCoupons(String search, Integer status) {
        if (connection == null) {
            return 0;
        }

        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM coupons WHERE deleted = 0"
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (code LIKE ? OR name LIKE ? OR description LIKE ?)");
        }

        if (status != null) {
            sql.append(" AND status = ?");
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int paramIndex = 1;

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }

            if (status != null) {
                ps.setInt(paramIndex, status);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public Coupon getCouponById(int id) {
        if (connection == null) {
            return null;
        }
        String sql = "SELECT * FROM coupons WHERE id = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCoupon(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean deleteCoupon(int id) {
        if (connection == null) {
            return false;
        }
        String sql = "UPDATE coupons SET deleted = 1, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean restoreCoupon(int id) {
        if (connection == null) {
            return false;
        }
        String sql = "UPDATE coupons SET deleted = 0, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateCouponStatus(int id, int status) {
        if (connection == null) {
            return false;
        }
        String sql = "UPDATE coupons SET status = ?, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checkCouponCodeExists(String code) {
        if (connection == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM coupons WHERE code = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public int createCoupon(Coupon coupon) {
        if (connection == null) {
            return 0;
        }
        
        StringBuilder sql = new StringBuilder(
            "INSERT INTO coupons (code, name, description, discount_type, discount_value, "
            + "min_order_amount, max_discount_amount, start_date, end_date, "
            + "usage_limit_per_user, total_usage_limit, current_usage_count, status, "
            + "applicable_product_ids, applicable_provider_ids, created_at, updated_at, deleted) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?, NOW(), NOW(), 0)"
        );
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, coupon.getCode());
            ps.setString(2, coupon.getName());
            ps.setString(3, coupon.getDescription());
            ps.setInt(4, coupon.getDiscount_type());
            ps.setDouble(5, coupon.getDiscount_value());
            ps.setDouble(6, coupon.getMin_order_amount());
            
            if (coupon.getMax_discount_amount() != null) {
                ps.setDouble(7, coupon.getMax_discount_amount());
            } else {
                ps.setNull(7, java.sql.Types.DOUBLE);
            }
            
            ps.setTimestamp(8, new java.sql.Timestamp(coupon.getStart_date().getTime()));
            ps.setTimestamp(9, new java.sql.Timestamp(coupon.getEnd_date().getTime()));
            ps.setInt(10, coupon.getUsage_limit_per_user());
            
            if (coupon.getTotal_usage_limit() != null) {
                ps.setInt(11, coupon.getTotal_usage_limit());
            } else {
                ps.setNull(11, java.sql.Types.INTEGER);
            }
            
            ps.setInt(12, coupon.getStatus());
            ps.setString(13, coupon.getApplicable_product_ids());
            ps.setString(14, coupon.getApplicable_provider_ids());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean updateCoupon(Coupon coupon) {
        if (connection == null) {
            return false;
        }
        
        StringBuilder sql = new StringBuilder(
            "UPDATE coupons SET code = ?, name = ?, description = ?, "
            + "discount_type = ?, discount_value = ?, min_order_amount = ?, "
            + "max_discount_amount = ?, start_date = ?, end_date = ?, "
            + "usage_limit_per_user = ?, total_usage_limit = ?, status = ?, "
            + "applicable_product_ids = ?, applicable_provider_ids = ?, "
            + "updated_at = NOW() WHERE id = ?"
        );
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            ps.setString(1, coupon.getCode());
            ps.setString(2, coupon.getName());
            ps.setString(3, coupon.getDescription());
            
            DiscountType discountType = DiscountType.fromValue(coupon.getDiscount_type());
            if (discountType != null) {
                ps.setString(4, discountType.getLabel());
            } else {
                ps.setString(4, DiscountType.PERCENTAGE.getLabel());
            }
            
            ps.setDouble(5, coupon.getDiscount_value());
            ps.setDouble(6, coupon.getMin_order_amount());
            
            if (coupon.getMax_discount_amount() != null) {
                ps.setDouble(7, coupon.getMax_discount_amount());
            } else {
                ps.setNull(7, java.sql.Types.DOUBLE);
            }
            
            ps.setTimestamp(8, new java.sql.Timestamp(coupon.getStart_date().getTime()));
            ps.setTimestamp(9, new java.sql.Timestamp(coupon.getEnd_date().getTime()));
            ps.setInt(10, coupon.getUsage_limit_per_user());
            
            if (coupon.getTotal_usage_limit() != null) {
                ps.setInt(11, coupon.getTotal_usage_limit());
            } else {
                ps.setNull(11, java.sql.Types.INTEGER);
            }
            
            CouponStatus status = CouponStatus.fromValue(coupon.getStatus());
            if (status != null) {
                ps.setString(12, status.getLabel());
            } else {
                ps.setString(12, CouponStatus.ACTIVE.getLabel());
            }
            
            ps.setString(13, coupon.getApplicable_product_ids());
            ps.setString(14, coupon.getApplicable_provider_ids());
            ps.setInt(15, coupon.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Coupon getCouponByCode(String code) {
        String sql = "SELECT * FROM coupons WHERE code = ? AND deleted = 0";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCoupon(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean canUserUseCoupon(int userId, int couponId) {
        String sql = "SELECT c.usage_limit_per_user, COUNT(cu.id) as used_count " +
                     "FROM coupons c " +
                     "LEFT JOIN coupon_usages cu ON c.id = cu.coupon_id AND cu.user_id = ? " +
                     "WHERE c.id = ? " +
                     "GROUP BY c.id, c.usage_limit_per_user";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, couponId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int usageLimit = rs.getInt("usage_limit_per_user");
                int usedCount = rs.getInt("used_count");
                return usedCount < usageLimit;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<Coupon> getAvailableCouponsForUser(int userId) {
        List<Coupon> coupons = new ArrayList<>();
        if (connection == null) {
            return coupons;
        }
        
        String sql = "SELECT c.*, " +
                     "COALESCE(COUNT(cu.id), 0) as user_usage_count " +
                     "FROM user_coupons uc " +
                     "INNER JOIN coupons c ON uc.coupon_id = c.id " +
                     "LEFT JOIN coupon_usages cu ON c.id = cu.coupon_id AND cu.user_id = ? " +
                     "WHERE uc.user_id = ? " +
                     "AND uc.status = 'available' " +
                     "AND c.status = 1 " +
                     "AND c.deleted = 0 " +
                     "AND c.start_date <= NOW() " +
                     "AND c.end_date >= NOW() " +
                     "AND (uc.expires_at IS NULL OR uc.expires_at >= NOW()) " +
                     "AND (c.total_usage_limit IS NULL OR c.current_usage_count < c.total_usage_limit) " +
                     "GROUP BY c.id " +
                     "HAVING user_usage_count < c.usage_limit_per_user " +
                     "ORDER BY c.discount_value DESC";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return coupons;
    }
    
    public List<Coupon> getAvailableCouponsForUser(int userId, int limit, int offset) {
        List<Coupon> coupons = new ArrayList<>();
        if (connection == null) {
            return coupons;
        }
        
        String sql = "SELECT c.*, " +
                     "COALESCE(COUNT(cu.id), 0) as user_usage_count " +
                     "FROM user_coupons uc " +
                     "INNER JOIN coupons c ON uc.coupon_id = c.id " +
                     "LEFT JOIN coupon_usages cu ON c.id = cu.coupon_id AND cu.user_id = ? " +
                     "WHERE uc.user_id = ? " +
                     "AND uc.status = 'available' " +
                     "AND c.status = 1 " +
                     "AND c.deleted = 0 " +
                     "AND c.start_date <= NOW() " +
                     "AND c.end_date >= NOW() " +
                     "AND (uc.expires_at IS NULL OR uc.expires_at >= NOW()) " +
                     "AND (c.total_usage_limit IS NULL OR c.current_usage_count < c.total_usage_limit) " +
                     "GROUP BY c.id " +
                     "HAVING user_usage_count < c.usage_limit_per_user " +
                     "ORDER BY c.discount_value DESC " +
                     "LIMIT ? OFFSET ?";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return coupons;
    }
    
    public int countAvailableCouponsForUser(int userId) {
        if (connection == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(DISTINCT c.id) as total " +
                     "FROM user_coupons uc " +
                     "INNER JOIN coupons c ON uc.coupon_id = c.id " +
                     "LEFT JOIN coupon_usages cu ON c.id = cu.coupon_id AND cu.user_id = ? " +
                     "WHERE uc.user_id = ? " +
                     "AND uc.status = 'available' " +
                     "AND c.status = 1 " +
                     "AND c.deleted = 0 " +
                     "AND c.start_date <= NOW() " +
                     "AND c.end_date >= NOW() " +
                     "AND (uc.expires_at IS NULL OR uc.expires_at >= NOW()) " +
                     "AND (c.total_usage_limit IS NULL OR c.current_usage_count < c.total_usage_limit) " +
                     "GROUP BY c.id " +
                     "HAVING COALESCE(COUNT(cu.id), 0) < c.usage_limit_per_user";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            return count;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public boolean assignCouponToUser(int userId, int couponId, String expiresAt) {
        String sql = "INSERT INTO user_coupons (user_id, coupon_id, status, expires_at) " +
                     "VALUES (?, ?, 'available', ?) " +
                     "ON DUPLICATE KEY UPDATE status = 'available', expires_at = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, couponId);
            ps.setString(3, expiresAt);
            ps.setString(4, expiresAt);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean assignCouponToAllUsers(int couponId, String expiresAt) {
        String sql = "INSERT INTO user_coupons (user_id, coupon_id, status, expires_at) " +
                     "SELECT u.id, ?, 'available', ? " +
                     "FROM users u " +
                     "WHERE u.deleted = 0 AND u.role = 'user' " +
                     "ON DUPLICATE KEY UPDATE status = 'available', expires_at = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setString(2, expiresAt);
            ps.setString(3, expiresAt);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean canUserUseCouponFromUserCoupons(int userId, int couponId) {
        String sql = "SELECT uc.status, c.usage_limit_per_user, COUNT(cu.id) as used_count " +
                     "FROM user_coupons uc " +
                     "INNER JOIN coupons c ON uc.coupon_id = c.id " +
                     "LEFT JOIN coupon_usages cu ON c.id = cu.coupon_id AND cu.user_id = ? " +
                     "WHERE uc.user_id = ? " +
                     "AND uc.coupon_id = ? " +
                     "AND uc.status = 'available' " +
                     "AND c.status = 1 " +
                     "AND c.deleted = 0 " +
                     "AND (uc.expires_at IS NULL OR uc.expires_at >= NOW()) " +
                     "GROUP BY uc.id, c.usage_limit_per_user";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, couponId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int usageLimit = rs.getInt("usage_limit_per_user");
                int usedCount = rs.getInt("used_count");
                return usedCount < usageLimit;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
