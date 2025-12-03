/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum OrderStatus {
    PENDING(1, "pending"),
    PROCESSING(2, "processing"),
    COMPLETED(3, "completed"),
    CANCELLED(4, "cancelled"),
    REFUNDED(5, "refunded");
    
    private final int value;
    private final String label;
    
    OrderStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static OrderStatus fromValue(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static OrderStatus fromLabel(String label) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

