package dal;

import model.PaymentTransaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentTransactionDao extends DBContext {

    public List<PaymentTransaction> getTransactionsByUserId(int userId, int limit, int offset) {
        List<PaymentTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM payment_transactions WHERE user_id = ? AND deleted = 0 ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            Logger.getLogger(PaymentTransactionDao.class.getName()).log(Level.SEVERE, "Error getting transactions by user id", e);
        }
        
        return transactions;
    }

    public int countTransactionsByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM payment_transactions WHERE user_id = ? AND deleted = 0";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(PaymentTransactionDao.class.getName()).log(Level.SEVERE, "Error counting transactions by user id", e);
        }
        
        return 0;
    }

    private PaymentTransaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setId(rs.getInt("id"));
        transaction.setUser_id(rs.getInt("user_id"));
        transaction.setTransaction_code(rs.getString("transaction_code"));
        
        String typeStr = rs.getString("type");
        if ("deposit".equals(typeStr)) {
            transaction.setType(1);
        } else if ("purchase".equals(typeStr)) {
            transaction.setType(2);
        } else if ("refund".equals(typeStr)) {
            transaction.setType(3);
        } else {
            transaction.setType(0);
        }
        
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setBalance_before(rs.getDouble("balance_before"));
        transaction.setBalance_after(rs.getDouble("balance_after"));
        
        String statusStr = rs.getString("status");
        if ("pending".equals(statusStr)) {
            transaction.setStatus(0);
        } else if ("completed".equals(statusStr)) {
            transaction.setStatus(1);
        } else if ("failed".equals(statusStr)) {
            transaction.setStatus(2);
        } else {
            transaction.setStatus(0);
        }
        
        transaction.setDescription(rs.getString("description"));
        transaction.setCreated_at(rs.getTimestamp("created_at"));
        transaction.setUpdated_at(rs.getTimestamp("updated_at"));
        transaction.setDeleted(rs.getInt("deleted"));
        
        return transaction;
    }
}

