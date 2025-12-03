/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum DiscountType {
    PERCENTAGE(1, "percentage"),
    FIXED_AMOUNT(2, "fixed_amount");
    
    private final int value;
    private final String label;
    
    DiscountType(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static DiscountType fromValue(int value) {
        for (DiscountType type : DiscountType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
    
    public static DiscountType fromLabel(String label) {
        for (DiscountType type : DiscountType.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        return null;
    }
}

