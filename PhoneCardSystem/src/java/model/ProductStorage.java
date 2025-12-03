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
public class ProductStorage {
    private int id;
    private int product_id;
    private String card_code;
    private String card_serial;
    private String pin_code;
    private int status;
    private Date sold_at;
    private Date used_at;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public ProductStorage() {
    }

    public ProductStorage(int id, int product_id, String card_code, String card_serial, String pin_code, int status, Date sold_at, Date used_at, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.product_id = product_id;
        this.card_code = card_code;
        this.card_serial = card_serial;
        this.pin_code = pin_code;
        this.status = status;
        this.sold_at = sold_at;
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

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public String getCard_serial() {
        return card_serial;
    }

    public void setCard_serial(String card_serial) {
        this.card_serial = card_serial;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getSold_at() {
        return sold_at;
    }

    public void setSold_at(Date sold_at) {
        this.sold_at = sold_at;
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
        return "ProductStorage{" + "id=" + id + ", product_id=" + product_id + ", card_code=" + card_code + ", card_serial=" + card_serial + ", pin_code=" + pin_code + ", status=" + status + ", sold_at=" + sold_at + ", used_at=" + used_at + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

