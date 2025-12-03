/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum ProductType {
    TOPUP(1, "topup"),
    DATA_PACKAGE(2, "data_package");
    
    private final int value;
    private final String label;
    
    ProductType(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static ProductType fromValue(int value) {
        for (ProductType type : ProductType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
    
    public static ProductType fromLabel(String label) {
        for (ProductType type : ProductType.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        return null;
    }
}

