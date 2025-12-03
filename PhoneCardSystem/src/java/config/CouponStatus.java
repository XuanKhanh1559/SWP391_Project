/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum CouponStatus {
    ACTIVE(1, "active"),
    INACTIVE(2, "inactive"),
    EXPIRED(3, "expired"),
    USED_UP(4, "used_up");
    
    private final int value;
    private final String label;
    
    CouponStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static CouponStatus fromValue(int value) {
        for (CouponStatus status : CouponStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static CouponStatus fromLabel(String label) {
        for (CouponStatus status : CouponStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

