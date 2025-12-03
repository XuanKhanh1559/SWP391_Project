/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Date;

/**
 *
 * @author admin
 */
public class PaymentTransaction {
    private int id;
    private int user_id;
    private String transaction_code;
    private int type;
    private double amount;
    private double balance_before;
    private double balance_after;
    private int status;
    private String description;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public PaymentTransaction() {
    }

    public PaymentTransaction(int id, int user_id, String transaction_code, int type, double amount, double balance_before, double balance_after, int status, String description, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.user_id = user_id;
        this.transaction_code = transaction_code;
        this.type = type;
        this.amount = amount;
        this.balance_before = balance_before;
        this.balance_after = balance_after;
        this.status = status;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance_before() {
        return balance_before;
    }

    public void setBalance_before(double balance_before) {
        this.balance_before = balance_before;
    }

    public double getBalance_after() {
        return balance_after;
    }

    public void setBalance_after(double balance_after) {
        this.balance_after = balance_after;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "PaymentTransaction{" + "id=" + id + ", user_id=" + user_id + ", transaction_code=" + transaction_code + ", type=" + type + ", amount=" + amount + ", balance_before=" + balance_before + ", balance_after=" + balance_after + ", status=" + status + ", description=" + description + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

