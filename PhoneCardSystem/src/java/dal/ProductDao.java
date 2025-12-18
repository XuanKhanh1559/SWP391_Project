package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import config.ProductStatus;
import config.ProductType;

public class ProductDao extends DBContext {

    private String lastErrorMessage;

    public String getLastError() {
        return lastErrorMessage;
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
        product.setStock(rs.getInt("stock"));
        
        product.setCreated_at(rs.getTimestamp("created_at"));
        product.setUpdated_at(rs.getTimestamp("updated_at"));
        product.setDeleted(rs.getInt("deleted"));
        return product;
    }

    private String buildWhereClause(Integer providerId, Integer type, String search, boolean isAdmin, List<Object> params) {
        StringBuilder whereClause = new StringBuilder();
        
        if (!isAdmin) {
            whereClause.append("AND (p.status = ? OR p.status = ?) AND p.deleted = 0 ");
            params.add(ProductStatus.ACTIVE.getValue());
            params.add(ProductStatus.ACTIVE.getLabel());
        }

        if (providerId != null && providerId > 0) {
            whereClause.append("AND p.provider_id = ? ");
            params.add(providerId);
        }

        if (type != null && type > 0) {
            ProductType productType = ProductType.fromValue(type);
            if (productType != null) {
                whereClause.append("AND (p.type = ? OR p.type = ?) ");
                params.add(type);
                params.add(productType.getLabel());
            }
        }

        if (search != null && !search.trim().isEmpty()) {
            whereClause.append("AND p.name LIKE ? ");
            params.add("%" + search.trim() + "%");
        }
        
        return whereClause.toString();
    }

    public List<Product> getAllActiveProducts() {
        List<Product> products = new ArrayList<>();
        if (connection == null) {
            return products;
        }
        String sql = "SELECT id, name, denomination FROM products WHERE status = 'active' AND deleted = 0 ORDER BY name";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDenomination(rs.getDouble("denomination"));
                products.add(product);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    public List<Product> getAllProducts(Integer providerId, Integer type, String search, boolean isAdmin, int page, int pageSize) {
        List<Product> products = new ArrayList<>();
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return products;
        }

        List<Object> params = new ArrayList<>();
        String whereClause = buildWhereClause(providerId, type, search, isAdmin, params);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, p.provider_id, p.name, p.type, p.denomination, p.price, p.description, p.status, p.stock, p.created_at, p.updated_at, p.deleted ");
        sql.append("FROM products p ");
        sql.append("INNER JOIN providers pr ON p.provider_id = pr.id ");
        sql.append("WHERE pr.deleted = 0 ");
        sql.append(whereClause);
        sql.append("ORDER BY p.id ASC ");
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
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi lấy danh sách sản phẩm: " + ex.getMessage();
        }
        return products;
    }

    public int countProducts(Integer providerId, Integer type, String search, boolean isAdmin) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return 0;
        }

        List<Object> params = new ArrayList<>();
        String whereClause = buildWhereClause(providerId, type, search, isAdmin, params);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) as total FROM products p ");
        sql.append("INNER JOIN providers pr ON p.provider_id = pr.id ");
        sql.append("WHERE pr.deleted = 0 ");
        sql.append(whereClause);

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
            lastErrorMessage = "Lỗi khi đếm số lượng sản phẩm: " + ex.getMessage();
        }
        return 0;
    }

    public Product getProductById(int productId, boolean isAdmin) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return null;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, provider_id, name, type, denomination, price, description, status, stock, created_at, updated_at, deleted ");
        sql.append("FROM products WHERE id = ? ");

        if (!isAdmin) {
            sql.append("AND (status = ? OR status = ?) AND deleted = 0");
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            ps.setInt(1, productId);
            if (!isAdmin) {
                ps.setInt(2, ProductStatus.ACTIVE.getValue());
                ps.setString(3, ProductStatus.ACTIVE.getLabel());
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi lấy thông tin sản phẩm: " + ex.getMessage();
        }
        return null;
    }

    public boolean updateProduct(int productId, String name, double denomination, double price, String description, String status) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        if (price <= 0) {
            lastErrorMessage = "Giá sản phẩm phải lớn hơn 0";
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            lastErrorMessage = "Tên sản phẩm không được để trống";
            return false;
        }

        if (status == null || (!status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("inactive"))) {
            lastErrorMessage = "Trạng thái sản phẩm không hợp lệ";
            return false;
        }

        String sql = "UPDATE products SET name = ?, denomination = ?, price = ?, description = ?, status = ?, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setDouble(2, denomination);
            ps.setDouble(3, price);
            ps.setString(4, description);
            ps.setString(5, status.toLowerCase());
            ps.setInt(6, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi cập nhật sản phẩm: " + ex.getMessage();
        }
        return false;
    }

    public boolean deleteProduct(int productId) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        String sql = "UPDATE products SET deleted = 1, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi xóa sản phẩm: " + ex.getMessage();
        }
        return false;
    }

    public boolean restoreProduct(int productId) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        String sql = "UPDATE products SET deleted = 0, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi khôi phục sản phẩm: " + ex.getMessage();
        }
        return false;
    }

    public boolean createProduct(int providerId, String name, int type, double denomination, double price, String description, String status) {
        if (connection == null) {
            lastErrorMessage = "Lỗi kết nối database. Vui lòng kiểm tra cấu hình database.";
            return false;
        }

        if (price <= 0) {
            lastErrorMessage = "Giá sản phẩm phải lớn hơn 0";
            return false;
        }

        if (denomination <= 0) {
            lastErrorMessage = "Mệnh giá sản phẩm phải lớn hơn 0";
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            lastErrorMessage = "Tên sản phẩm không được để trống";
            return false;
        }

        if (status == null || (!status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("inactive"))) {
            lastErrorMessage = "Trạng thái sản phẩm không hợp lệ";
            return false;
        }

        if (providerId < 1 || providerId > 3) {
            lastErrorMessage = "Nhà mạng không hợp lệ";
            return false;
        }

        if (type < 1 || type > 2) {
            lastErrorMessage = "Loại sản phẩm không hợp lệ";
            return false;
        }

        String sql = "INSERT INTO products (provider_id, name, type, denomination, price, description, status, created_at, updated_at, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), 0)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, providerId);
            ps.setString(2, name.trim());
            ps.setInt(3, type);
            ps.setDouble(4, denomination);
            ps.setDouble(5, price);
            ps.setString(6, description);
            ps.setString(7, status.toLowerCase());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastErrorMessage = "Lỗi khi tạo sản phẩm: " + ex.getMessage();
        }
        return false;
    }
}
