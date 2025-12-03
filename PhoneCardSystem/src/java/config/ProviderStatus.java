/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum ProviderStatus {
    ACTIVE(1, "active"),
    INACTIVE(2, "inactive");
    
    private final int value;
    private final String label;
    
    ProviderStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static ProviderStatus fromValue(int value) {
        for (ProviderStatus status : ProviderStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static ProviderStatus fromLabel(String label) {
        for (ProviderStatus status : ProviderStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

