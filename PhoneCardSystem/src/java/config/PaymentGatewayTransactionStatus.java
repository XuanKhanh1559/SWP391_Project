/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum PaymentGatewayTransactionStatus {
    PENDING(1, "pending"),
    PROCESSING(2, "processing"),
    SUCCESS(3, "success"),
    FAILED(4, "failed"),
    CANCELLED(5, "cancelled");
    
    private final int value;
    private final String label;
    
    PaymentGatewayTransactionStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static PaymentGatewayTransactionStatus fromValue(int value) {
        for (PaymentGatewayTransactionStatus status : PaymentGatewayTransactionStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static PaymentGatewayTransactionStatus fromLabel(String label) {
        for (PaymentGatewayTransactionStatus status : PaymentGatewayTransactionStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

