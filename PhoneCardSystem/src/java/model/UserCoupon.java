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
public class UserCoupon {
    private int id;
    private int user_id;
    private int coupon_id;
    private int status;
    private Date used_at;
    private Date expires_at;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public UserCoupon() {
    }

    public UserCoupon(int id, int user_id, int coupon_id, int status, Date used_at, Date expires_at, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.user_id = user_id;
        this.coupon_id = coupon_id;
        this.status = status;
        this.used_at = used_at;
        this.expires_at = expires_at;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getUsed_at() {
        return used_at;
    }

    public void setUsed_at(Date used_at) {
        this.used_at = used_at;
    }

    public Date getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Date expires_at) {
        this.expires_at = expires_at;
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
        return "UserCoupon{" + "id=" + id + ", user_id=" + user_id + ", coupon_id=" + coupon_id + ", status=" + status + ", used_at=" + used_at + ", expires_at=" + expires_at + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

