package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Provider;

public class ProviderDao extends DBContext {

    public List<Provider> getAllActiveProviders() {
        List<Provider> providers = new ArrayList<>();
        if (connection == null) {
            return providers;
        }
        String sql = "SELECT id, name, code FROM providers WHERE status = 'active' AND deleted = 0 ORDER BY name";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Provider provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setName(rs.getString("name"));
                provider.setCode(rs.getString("code"));
                providers.add(provider);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return providers;
    }

    public List<Provider> getAllProviders(String search, Integer status, Integer deleted, int page, int pageSize) {
        List<Provider> providers = new ArrayList<>();
        if (connection == null) {
            return providers;
        }
        
        StringBuilder sql = new StringBuilder("SELECT * FROM providers WHERE 1=1");
        
        if (deleted != null) {
            sql.append(" AND deleted = ?");
        } else {
            sql.append(" AND deleted = 0");
        }
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR code LIKE ? OR description LIKE ?)");
        }
        
        if (status != null) {
            sql.append(" AND status = ?");
        }
        
        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int paramIndex = 1;
            
            if (deleted != null) {
                ps.setInt(paramIndex++, deleted);
            }
            
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            if (status != null) {
                ps.setString(paramIndex++, status == 1 ? "active" : "inactive");
            }
            
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page - 1) * pageSize);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                providers.add(mapResultSetToProvider(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error getting all providers", ex);
        }
        return providers;
    }

    public int countProviders(String search, Integer status, Integer deleted) {
        if (connection == null) {
            return 0;
        }
        
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM providers WHERE 1=1");
        
        if (deleted != null) {
            sql.append(" AND deleted = ?");
        } else {
            sql.append(" AND deleted = 0");
        }
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR code LIKE ? OR description LIKE ?)");
        }
        
        if (status != null) {
            sql.append(" AND status = ?");
        }
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int paramIndex = 1;
            
            if (deleted != null) {
                ps.setInt(paramIndex++, deleted);
            }
            
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            if (status != null) {
                ps.setString(paramIndex++, status == 1 ? "active" : "inactive");
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error counting providers", ex);
        }
        return 0;
    }

    public Provider getProviderById(int id) {
        return getProviderById(id, false);
    }

    public Provider getProviderById(int id, boolean includeDeleted) {
        if (connection == null) {
            return null;
        }
        
        String sql = "SELECT * FROM providers WHERE id = ?";
        if (!includeDeleted) {
            sql += " AND deleted = 0";
        }
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToProvider(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error getting provider by id", ex);
        }
        return null;
    }

    public Provider getProviderByCode(String code) {
        if (connection == null) {
            return null;
        }
        
        String sql = "SELECT * FROM providers WHERE code = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToProvider(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error getting provider by code", ex);
        }
        return null;
    }

    public boolean createProvider(Provider provider) {
        if (connection == null) {
            return false;
        }
        
        String sql = "INSERT INTO providers (name, code, description, status) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, provider.getName());
            ps.setString(2, provider.getCode());
            ps.setString(3, provider.getDescription());
            ps.setString(4, provider.getStatus() == 1 ? "active" : "inactive");
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error creating provider", ex);
        }
        return false;
    }

    public boolean updateProvider(Provider provider) {
        if (connection == null) {
            return false;
        }
        
        String sql = "UPDATE providers SET name = ?, code = ?, description = ?, status = ?, updated_at = NOW() WHERE id = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, provider.getName());
            ps.setString(2, provider.getCode());
            ps.setString(3, provider.getDescription());
            ps.setString(4, provider.getStatus() == 1 ? "active" : "inactive");
            ps.setInt(5, provider.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error updating provider", ex);
        }
        return false;
    }

    public boolean deleteProvider(int id) {
        if (connection == null) {
            return false;
        }
        
        String sql = "UPDATE providers SET deleted = 1, updated_at = NOW() WHERE id = ? AND deleted = 0";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error deleting provider", ex);
        }
        return false;
    }

    public boolean restoreProvider(int id) {
        if (connection == null) {
            return false;
        }
        
        String sql = "UPDATE providers SET deleted = 0, updated_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProviderDao.class.getName()).log(Level.SEVERE, "Error restoring provider", ex);
        }
        return false;
    }

    private Provider mapResultSetToProvider(ResultSet rs) throws SQLException {
        Provider provider = new Provider();
        provider.setId(rs.getInt("id"));
        provider.setName(rs.getString("name"));
        provider.setCode(rs.getString("code"));
        provider.setDescription(rs.getString("description"));
        
        String statusStr = rs.getString("status");
        provider.setStatus("active".equals(statusStr) ? 1 : 0);
        
        provider.setCreated_at(rs.getTimestamp("created_at"));
        provider.setUpdated_at(rs.getTimestamp("updated_at"));
        provider.setDeleted(rs.getInt("deleted"));
        return provider;
    }
}

