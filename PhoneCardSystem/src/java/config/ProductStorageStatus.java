/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum ProductStorageStatus {
    AVAILABLE(1, "available"),
    SOLD(2, "sold"),
    USED(3, "used"),
    EXPIRED(4, "expired"),
    INVALID(5, "invalid");
    
    private final int value;
    private final String label;
    
    ProductStorageStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static ProductStorageStatus fromValue(int value) {
        for (ProductStorageStatus status : ProductStorageStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static ProductStorageStatus fromLabel(String label) {
        for (ProductStorageStatus status : ProductStorageStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

