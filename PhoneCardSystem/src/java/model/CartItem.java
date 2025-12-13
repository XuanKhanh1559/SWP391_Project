package model;

import java.sql.Timestamp;

public class CartItem {
    private int id;
    private int user_id;
    private int product_id;
    private int quantity;
    private double unit_price;
    private Timestamp created_at;
    private Timestamp updated_at;
    private int deleted;
    
    private Product product;

    public CartItem() {
    }

    public CartItem(int id, int user_id, int product_id, int quantity, double unit_price, Timestamp created_at, Timestamp updated_at, int deleted) {
        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
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

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getTotalPrice() {
        return unit_price * quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" + "id=" + id + ", user_id=" + user_id + ", product_id=" + product_id + ", quantity=" + quantity + ", unit_price=" + unit_price + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}


