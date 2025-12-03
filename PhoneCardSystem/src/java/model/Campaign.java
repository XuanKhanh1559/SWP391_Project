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
public class Campaign {
    private int id;
    private String name;
    private String description;
    private Date start_date;
    private Date end_date;
    private int status;
    private int discount_type;
    private double discount_value;
    private double min_order_amount;
    private Double max_discount_amount;
    private int usage_limit_per_user;
    private Integer total_usage_limit;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public Campaign() {
    }

    public Campaign(int id, String name, String description, Date start_date, Date end_date, int status, int discount_type, double discount_value, double min_order_amount, Double max_discount_amount, int usage_limit_per_user, Integer total_usage_limit, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.discount_type = discount_type;
        this.discount_value = discount_value;
        this.min_order_amount = min_order_amount;
        this.max_discount_amount = max_discount_amount;
        this.usage_limit_per_user = usage_limit_per_user;
        this.total_usage_limit = total_usage_limit;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(int discount_type) {
        this.discount_type = discount_type;
    }

    public double getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(double discount_value) {
        this.discount_value = discount_value;
    }

    public double getMin_order_amount() {
        return min_order_amount;
    }

    public void setMin_order_amount(double min_order_amount) {
        this.min_order_amount = min_order_amount;
    }

    public Double getMax_discount_amount() {
        return max_discount_amount;
    }

    public void setMax_discount_amount(Double max_discount_amount) {
        this.max_discount_amount = max_discount_amount;
    }

    public int getUsage_limit_per_user() {
        return usage_limit_per_user;
    }

    public void setUsage_limit_per_user(int usage_limit_per_user) {
        this.usage_limit_per_user = usage_limit_per_user;
    }

    public Integer getTotal_usage_limit() {
        return total_usage_limit;
    }

    public void setTotal_usage_limit(Integer total_usage_limit) {
        this.total_usage_limit = total_usage_limit;
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
        return "Campaign{" + "id=" + id + ", name=" + name + ", description=" + description + ", start_date=" + start_date + ", end_date=" + end_date + ", status=" + status + ", discount_type=" + discount_type + ", discount_value=" + discount_value + ", min_order_amount=" + min_order_amount + ", max_discount_amount=" + max_discount_amount + ", usage_limit_per_user=" + usage_limit_per_user + ", total_usage_limit=" + total_usage_limit + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

