/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum UserCouponStatus {
    AVAILABLE(1, "available"),
    USED(2, "used"),
    EXPIRED(3, "expired");
    
    private final int value;
    private final String label;
    
    UserCouponStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static UserCouponStatus fromValue(int value) {
        for (UserCouponStatus status : UserCouponStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static UserCouponStatus fromLabel(String label) {
        for (UserCouponStatus status : UserCouponStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

