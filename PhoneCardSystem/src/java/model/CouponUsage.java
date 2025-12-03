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
public class CouponUsage {
    private int id;
    private int user_id;
    private int coupon_id;
    private int order_id;
    private Integer user_coupon_id;
    private double discount_amount;
    private double order_amount_before_discount;
    private double order_amount_after_discount;
    private Date used_at;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public CouponUsage() {
    }

    public CouponUsage(int id, int user_id, int coupon_id, int order_id, Integer user_coupon_id, double discount_amount, double order_amount_before_discount, double order_amount_after_discount, Date used_at, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.user_id = user_id;
        this.coupon_id = coupon_id;
        this.order_id = order_id;
        this.user_coupon_id = user_coupon_id;
        this.discount_amount = discount_amount;
        this.order_amount_before_discount = order_amount_before_discount;
        this.order_amount_after_discount = order_amount_after_discount;
        this.used_at = used_at;
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

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Integer getUser_coupon_id() {
        return user_coupon_id;
    }

    public void setUser_coupon_id(Integer user_coupon_id) {
        this.user_coupon_id = user_coupon_id;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getOrder_amount_before_discount() {
        return order_amount_before_discount;
    }

    public void setOrder_amount_before_discount(double order_amount_before_discount) {
        this.order_amount_before_discount = order_amount_before_discount;
    }

    public double getOrder_amount_after_discount() {
        return order_amount_after_discount;
    }

    public void setOrder_amount_after_discount(double order_amount_after_discount) {
        this.order_amount_after_discount = order_amount_after_discount;
    }

    public Date getUsed_at() {
        return used_at;
    }

    public void setUsed_at(Date used_at) {
        this.used_at = used_at;
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
        return "CouponUsage{" + "id=" + id + ", user_id=" + user_id + ", coupon_id=" + coupon_id + ", order_id=" + order_id + ", user_coupon_id=" + user_coupon_id + ", discount_amount=" + discount_amount + ", order_amount_before_discount=" + order_amount_before_discount + ", order_amount_after_discount=" + order_amount_after_discount + ", used_at=" + used_at + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

