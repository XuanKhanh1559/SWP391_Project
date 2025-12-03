/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum CampaignStatus {
    DRAFT(1, "draft"),
    ACTIVE(2, "active"),
    PAUSED(3, "paused"),
    ENDED(4, "ended");
    
    private final int value;
    private final String label;
    
    CampaignStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static CampaignStatus fromValue(int value) {
        for (CampaignStatus status : CampaignStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
    
    public static CampaignStatus fromLabel(String label) {
        for (CampaignStatus status : CampaignStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return null;
    }
}

