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
public class Order {
    private int id;
    private int user_id;
    private int payment_transaction_id;
    private Integer coupon_id;
    private String order_code;
    private double subtotal_amount;
    private double discount_amount;
    private double total_amount;
    private int status;
    private String notes;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public Order() {
    }

    public Order(int id, int user_id, int payment_transaction_id, Integer coupon_id, String order_code, double subtotal_amount, double discount_amount, double total_amount, int status, String notes, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.user_id = user_id;
        this.payment_transaction_id = payment_transaction_id;
        this.coupon_id = coupon_id;
        this.order_code = order_code;
        this.subtotal_amount = subtotal_amount;
        this.discount_amount = discount_amount;
        this.total_amount = total_amount;
        this.status = status;
        this.notes = notes;
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

    public int getPayment_transaction_id() {
        return payment_transaction_id;
    }

    public void setPayment_transaction_id(int payment_transaction_id) {
        this.payment_transaction_id = payment_transaction_id;
    }

    public Integer getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Integer coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public double getSubtotal_amount() {
        return subtotal_amount;
    }

    public void setSubtotal_amount(double subtotal_amount) {
        this.subtotal_amount = subtotal_amount;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        return "Order{" + "id=" + id + ", user_id=" + user_id + ", payment_transaction_id=" + payment_transaction_id + ", coupon_id=" + coupon_id + ", order_code=" + order_code + ", subtotal_amount=" + subtotal_amount + ", discount_amount=" + discount_amount + ", total_amount=" + total_amount + ", status=" + status + ", notes=" + notes + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

