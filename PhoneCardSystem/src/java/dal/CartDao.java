package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Product;
import config.ProductStatus;
import config.ProductType;

public class CartDao extends DBContext {

    private String lastErrorMessage;

    public String getLastError() {
        return lastErrorMessage;
    }

    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setId(rs.getInt("id"));
        cartItem.setUser_id(rs.getInt("user_id"));
        cartItem.setProduct_id(rs.getInt("product_id"));
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItem.setUnit_price(rs.getDouble("unit_price"));
        cartItem.setCreated_at(rs.getTimestamp("created_at"));
        cartItem.setUpdated_at(rs.getTimestamp("updated_at"));
        cartItem.setDeleted(rs.getInt("deleted"));
        return cartItem;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setProvider_id(rs.getInt("provider_id"));
        product.setName(rs.getString("name"));
        
        Object typeObj = rs.getObject("type");
        int typeValue;
        if (typeObj instanceof String) {
            ProductType productType = ProductType.fromLabel((String) typeObj);
            typeValue = productType != null ? productType.getValue() : 1;
        } else if (typeObj instanceof Number) {
            typeValue = ((Number) typeObj).intValue();
        } else {
            typeValue = 1;
        }
        product.setType(typeValue);
        
        product.setDenomination(rs.getDouble("denomination"));
        product.setPrice(rs.getDouble("price"));
        product.setDescription(rs.getString("description"));
        
        Object statusObj = rs.getObject("status");
        int statusValue;
        if (statusObj instanceof String) {
            ProductStatus productStatus = ProductStatus.fromLabel((String) statusObj);
            statusValue = productStatus != null ? productStatus.getValue() : 1;
        } else if (statusObj instanceof Number) {
            statusValue = ((Number) statusObj).intValue();
        } else {
            statusValue = 1;
        }
        product.setStatus(statusValue);
        
        product.setCreated_at(rs.getTimestamp("created_at"));
        product.setUpdated_at(rs.getTimestamp("updated_at"));
        product.setDeleted(rs.getInt("deleted"));
        return product;
    }

    public Product getProductForCart(int productId) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return null;
        }

        String sql = "SELECT id, provider_id, name, type, denomination, price, description, status, created_at, updated_at, deleted "
                   + "FROM products WHERE id = ? AND (status = ? OR status = ?) AND deleted = 0";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, productId);
            ps.setInt(2, ProductStatus.ACTIVE.getValue());
            ps.setString(3, ProductStatus.ACTIVE.getLabel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi kiểm tra sản phẩm: " + ex.getMessage();
        }
        return null;
    }

    public boolean validateProductForCart(int productId) {
        Product product = getProductForCart(productId);
        if (product == null) {
            lastErrorMessage = "Sản phẩm không tồn tại hoặc không khả dụng";
            return false;
        }
        
        if (product.getStatus() != ProductStatus.ACTIVE.getValue()) {
            lastErrorMessage = "Sản phẩm không khả dụng";
            return false;
        }
        
        return true;
    }

    public boolean addToCart(int userId, int productId, int quantity, double unitPrice) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        String sql = "INSERT INTO cart_items (user_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE quantity = quantity + ?, unit_price = ?, updated_at = CURRENT_TIMESTAMP";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, unitPrice);
            ps.setInt(5, quantity);
            ps.setDouble(6, unitPrice);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi thêm vào giỏ hàng: " + ex.getMessage();
            return false;
        }
    }

    public List<CartItem> getCartByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return cartItems;
        }

        String sql = "SELECT id, user_id, product_id, quantity, unit_price, created_at, updated_at, deleted "
                   + "FROM cart_items WHERE user_id = ? AND deleted = 0 ORDER BY created_at DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            ProductDao productDao = new ProductDao();
            while (rs.next()) {
                CartItem cartItem = mapResultSetToCartItem(rs);
                Product product = productDao.getProductById(cartItem.getProduct_id(), false);
                cartItem.setProduct(product);
                cartItems.add(cartItem);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi lấy giỏ hàng: " + ex.getMessage();
        }
        return cartItems;
    }

    public int getCartCount(int userId) {
        if (connection == null) {
            return 0;
        }

        String sql = "SELECT SUM(quantity) as total FROM cart_items WHERE user_id = ? AND deleted = 0";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi đếm giỏ hàng: " + ex.getMessage();
        }
        return 0;
    }

    public boolean updateCartItemQuantity(int cartItemId, int quantity) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        if (quantity <= 0) {
            return removeCartItem(cartItemId);
        }

        String sql = "UPDATE cart_items SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND deleted = 0";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi cập nhật giỏ hàng: " + ex.getMessage();
            return false;
        }
    }

    public boolean removeCartItem(int cartItemId) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        String sql = "UPDATE cart_items SET deleted = 1, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cartItemId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi xóa khỏi giỏ hàng: " + ex.getMessage();
            return false;
        }
    }

    public boolean clearCart(int userId) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        String sql = "UPDATE cart_items SET deleted = 1, updated_at = CURRENT_TIMESTAMP WHERE user_id = ? AND deleted = 0";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            int result = ps.executeUpdate();
            return result >= 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi xóa giỏ hàng: " + ex.getMessage();
            return false;
        }
    }
}

