/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum PaymentTransactionStatus {
    PENDING(1, "pending"),
    COMPLETED(2, "completed"),
    FAILED(3, "failed"),
    CANCELLED(4, "cancelled");
    
    private final int value;
    private final String label;
    
    PaymentTransactionStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static PaymentTransactionStatus fromValue(int value) {
        for (PaymentTransactionStatus status : PaymentTransactionStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static PaymentTransactionStatus fromLabel(String label) {
        for (PaymentTransactionStatus status : PaymentTransactionStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

