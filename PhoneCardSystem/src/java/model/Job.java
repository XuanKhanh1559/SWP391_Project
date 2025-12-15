package model;

import java.sql.Timestamp;

public class Job {
    private int id;
    private String queue;
    private String payload;
    private int attempts;
    private int maxAttempts;
    private Timestamp reservedAt;
    private Timestamp availableAt;
    private Timestamp createdAt;
    private Timestamp completedAt;
    private Timestamp failedAt;
    private String status;
    private String errorMessage;

    public Job() {
    }

    public Job(String queue, String payload) {
        this.queue = queue;
        this.payload = payload;
        this.attempts = 0;
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Timestamp getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Timestamp reservedAt) {
        this.reservedAt = reservedAt;
    }

    public Timestamp getAvailableAt() {
        return availableAt;
    }

    public void setAvailableAt(Timestamp availableAt) {
        this.availableAt = availableAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public Timestamp getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(Timestamp failedAt) {
        this.failedAt = failedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

