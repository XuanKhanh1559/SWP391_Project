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
public class OrderItem {
    private int id;
    private int order_id;
    private int product_id;
    private String product_name_snapshot;
    private Integer product_storage_id;
    private int quantity;
    private double unit_price;
    private double total_price;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public OrderItem() {
    }

    public OrderItem(int id, int order_id, int product_id, Integer product_storage_id, int quantity, double unit_price, double total_price, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.product_storage_id = product_storage_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_price = total_price;
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

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name_snapshot() {
        return product_name_snapshot;
    }

    public void setProduct_name_snapshot(String product_name_snapshot) {
        this.product_name_snapshot = product_name_snapshot;
    }

    public Integer getProduct_storage_id() {
        return product_storage_id;
    }

    public void setProduct_storage_id(Integer product_storage_id) {
        this.product_storage_id = product_storage_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
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
        return "OrderItem{" + "id=" + id + ", order_id=" + order_id + ", product_id=" + product_id + ", product_storage_id=" + product_storage_id + ", quantity=" + quantity + ", unit_price=" + unit_price + ", total_price=" + total_price + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

