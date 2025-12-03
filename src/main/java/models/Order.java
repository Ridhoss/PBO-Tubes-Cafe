/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;

/**
 *
 * @author RIDHO
 */
public class Order {

    private Integer order_id;
    private Integer user_id;
    private LocalDateTime order_date;
    private Integer total_amount;
    private Integer discount;
    private Integer final_amount;
    private String payment_status;
    private String order_status;
    private String notes;

    public Order() {
    }

    public Order(Integer order_id, Integer user_id, LocalDateTime order_date, Integer total_amount,
            Integer discount, Integer final_amount, String payment_status,
            String order_status, String notes) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.discount = discount;
        this.final_amount = final_amount;
        this.payment_status = payment_status;
        this.order_status = order_status;
        this.notes = notes;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public LocalDateTime getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(Integer final_amount) {
        this.final_amount = final_amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
