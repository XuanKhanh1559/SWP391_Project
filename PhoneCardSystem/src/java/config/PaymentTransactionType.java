/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author admin
 */
public enum PaymentTransactionType {
    DEPOSIT(1, "deposit"),
    WITHDRAW(2, "withdraw"),
    PURCHASE(3, "purchase"),
    REFUND(4, "refund");
    
    private final int value;
    private final String label;
    
    PaymentTransactionType(int value, String label) {
        this.value = value;
        this.label = label;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static PaymentTransactionType fromValue(int value) {
        for (PaymentTransactionType type : PaymentTransactionType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
    
    public static PaymentTransactionType fromLabel(String label) {
        for (PaymentTransactionType type : PaymentTransactionType.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        return null;
    }
}

