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
public class PaymentGatewayTransaction {
    private int id;
    private int payment_transaction_id;
    private String gateway_name;
    private String gateway_transaction_id;
    private String gateway_transaction_code;
    private double amount;
    private String currency;
    private int status;
    private String gateway_response;
    private String callback_data;
    private Date created_at;
    private Date updated_at;
    private int deleted;

    public PaymentGatewayTransaction() {
    }

    public PaymentGatewayTransaction(int id, int payment_transaction_id, String gateway_name, String gateway_transaction_id, String gateway_transaction_code, double amount, String currency, int status, String gateway_response, String callback_data, Date created_at, Date updated_at, int deleted) {
        this.id = id;
        this.payment_transaction_id = payment_transaction_id;
        this.gateway_name = gateway_name;
        this.gateway_transaction_id = gateway_transaction_id;
        this.gateway_transaction_code = gateway_transaction_code;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.gateway_response = gateway_response;
        this.callback_data = callback_data;
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

    public int getPayment_transaction_id() {
        return payment_transaction_id;
    }

    public void setPayment_transaction_id(int payment_transaction_id) {
        this.payment_transaction_id = payment_transaction_id;
    }

    public String getGateway_name() {
        return gateway_name;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }

    public String getGateway_transaction_id() {
        return gateway_transaction_id;
    }

    public void setGateway_transaction_id(String gateway_transaction_id) {
        this.gateway_transaction_id = gateway_transaction_id;
    }

    public String getGateway_transaction_code() {
        return gateway_transaction_code;
    }

    public void setGateway_transaction_code(String gateway_transaction_code) {
        this.gateway_transaction_code = gateway_transaction_code;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGateway_response() {
        return gateway_response;
    }

    public void setGateway_response(String gateway_response) {
        this.gateway_response = gateway_response;
    }

    public String getCallback_data() {
        return callback_data;
    }

    public void setCallback_data(String callback_data) {
        this.callback_data = callback_data;
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
        return "PaymentGatewayTransaction{" + "id=" + id + ", payment_transaction_id=" + payment_transaction_id + ", gateway_name=" + gateway_name + ", gateway_transaction_id=" + gateway_transaction_id + ", gateway_transaction_code=" + gateway_transaction_code + ", amount=" + amount + ", currency=" + currency + ", status=" + status + ", gateway_response=" + gateway_response + ", callback_data=" + callback_data + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted=" + deleted + '}';
    }
}

