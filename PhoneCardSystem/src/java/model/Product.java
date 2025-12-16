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
public class Product {
    private int id;
    private int provider_id;
    private String name;
    private int type;
    private double denomination;
    private double price;
    private String description;
    private int status;
    private int stock;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public Product() {
    }

    public Product(int id, int provider_id, String name, int type, double denomination, double price, String description, int status, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.provider_id = provider_id;
        this.name = name;
        this.type = type;
        this.denomination = denomination;
        this.price = price;
        this.description = description;
        this.status = status;
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

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDenomination() {
        return denomination;
    }

    public void setDenomination(double denomination) {
        this.denomination = denomination;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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
        return "Product{" + "id=" + id + ", provider_id=" + provider_id + ", name=" + name + ", type=" + type + ", denomination=" + denomination + ", price=" + price + ", description=" + description + ", status=" + status + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

