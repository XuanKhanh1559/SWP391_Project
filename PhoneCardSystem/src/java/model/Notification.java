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
public class Notification {
    private int id;
    private Integer user_id;
    private int type;
    private String title;
    private String content;
    private String link;
    private boolean is_read;
    private Date read_at;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public Notification() {
    }

    public Notification(int id, Integer user_id, int type, String title, String content, String link, boolean is_read, Date read_at, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.link = link;
        this.is_read = is_read;
        this.read_at = read_at;
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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Date getRead_at() {
        return read_at;
    }

    public void setRead_at(Date read_at) {
        this.read_at = read_at;
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
        return "Notification{" + "id=" + id + ", user_id=" + user_id + ", type=" + type + ", title=" + title + ", content=" + content + ", link=" + link + ", is_read=" + is_read + ", read_at=" + read_at + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

