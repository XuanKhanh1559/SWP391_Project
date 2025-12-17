package dal;

import model.Job;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobDao extends DBContext {

    public int dispatchJob(String queue, String payload) {
        if (connection == null) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return -1;
        }
        
        String sql = "INSERT INTO jobs (queue, payload, attempts, status, available_at, created_at) VALUES (?, ?, 0, 'PENDING', NOW(), NOW())";
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, queue);
            ps.setString(2, payload);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Error dispatching job", ex);
        }
        return -1;
    }

    public Job getNextJob(String queue) {
        if (connection == null) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return null;
        }
        
        String sql = "SELECT * FROM jobs WHERE queue = ? AND status = 'PENDING' AND (reserved_at IS NULL OR reserved_at < DATE_SUB(NOW(), INTERVAL 5 MINUTE)) ORDER BY created_at ASC LIMIT 1 FOR UPDATE";
        Connection conn = null;
        try {
            conn = getNewConnection();
            conn.setAutoCommit(false);
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, queue);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Job job = mapResultSetToJob(rs);
                
                String updateSql = "UPDATE jobs SET status = 'PROCESSING', reserved_at = NOW(), attempts = attempts + 1 WHERE id = ?";
                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setInt(1, job.getId());
                updatePs.executeUpdate();
                
                conn.commit();
                return job;
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Error rolling back", e);
                }
            }
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Error getting next job", ex);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Error closing connection", e);
                }
            }
        }
        return null;
    }

    public void markCompleted(int jobId) {
        if (connection == null) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return;
        }
        
        String sql = "UPDATE jobs SET status = 'COMPLETED', completed_at = NOW() WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, jobId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Error marking job as completed", ex);
        }
    }

    public void markFailed(int jobId, String errorMessage) {
        if (connection == null) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return;
        }
        
        String sql = "UPDATE jobs SET status = 'FAILED', failed_at = NOW(), error_message = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, errorMessage);
            ps.setInt(2, jobId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Error marking job as failed", ex);
        }
    }

    public void retryJob(int jobId) {
        if (connection == null) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Connection is null");
            return;
        }
        
        String sql = "UPDATE jobs SET status = 'PENDING', reserved_at = NULL, available_at = DATE_ADD(NOW(), INTERVAL 5 SECOND) WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, jobId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JobDao.class.getName()).log(Level.SEVERE, "Error retrying job", ex);
        }
    }

    private Job mapResultSetToJob(ResultSet rs) throws SQLException {
        Job job = new Job();
        job.setId(rs.getInt("id"));
        job.setQueue(rs.getString("queue"));
        job.setPayload(rs.getString("payload"));
        job.setAttempts(rs.getInt("attempts"));
        job.setMaxAttempts(rs.getInt("max_attempts"));
        job.setReservedAt(rs.getTimestamp("reserved_at"));
        job.setAvailableAt(rs.getTimestamp("available_at"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        job.setCompletedAt(rs.getTimestamp("completed_at"));
        job.setFailedAt(rs.getTimestamp("failed_at"));
        job.setStatus(rs.getString("status"));
        job.setErrorMessage(rs.getString("error_message"));
        return job;
    }
}
