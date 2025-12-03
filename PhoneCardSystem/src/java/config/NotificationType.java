/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum NotificationType {
    ORDER(1, "order"),
    PAYMENT(2, "payment"),
    COUPON(3, "coupon"),
    PROMOTION(4, "promotion"),
    SYSTEM(5, "system"),
    ACCOUNT(6, "account");
    
    private final int value;
    private final String label;
    
    NotificationType(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static NotificationType fromValue(int value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
    
    public static NotificationType fromLabel(String label) {
        for (NotificationType type : NotificationType.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        return null;
    }
}

