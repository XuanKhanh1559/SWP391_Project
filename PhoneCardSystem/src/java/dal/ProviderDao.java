package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
}

