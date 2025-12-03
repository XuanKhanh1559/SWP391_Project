/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum ProductStatus {
    ACTIVE(1, "active"),
    INACTIVE(2, "inactive"),
    OUT_OF_STOCK(3, "out_of_stock");
    
    private final int value;
    private final String label;
    
    ProductStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static ProductStatus fromValue(int value) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static ProductStatus fromLabel(String label) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

