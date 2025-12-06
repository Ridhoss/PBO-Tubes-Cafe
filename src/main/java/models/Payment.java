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
public class Payment {

    private Integer payment_id;
    private Integer order_id;
    private String payment_method;
    private Integer amount_paid;
    private Integer change_amount;
    private LocalDateTime payment_time;
    private String info_payment;

    public Payment() {
    }

    public Payment(Integer payment_id, Integer order_id, String payment_method,
            Integer amount_paid, Integer change_amount, LocalDateTime payment_time, String info_payment) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.payment_method = payment_method;
        this.amount_paid = amount_paid;
        this.change_amount = change_amount;
        this.payment_time = payment_time;
        this.info_payment = info_payment;
    }

    public Integer getPayment_id() {
        return payment_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public Integer getAmount_paid() {
        return amount_paid;
    }

    public Integer getChange_amount() {
        return change_amount;
    }

    public LocalDateTime getPayment_time() {
        return payment_time;
    }
    
    public String getInfoPayment() {
        return info_payment;
    }

    public void setPayment_id(Integer payment_id) {
        this.payment_id = payment_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setAmount_paid(Integer amount_paid) {
        this.amount_paid = amount_paid;
    }

    public void setChange_amount(Integer change_amount) {
        this.change_amount = change_amount;
    }

    public void setPayment_time(LocalDateTime payment_time) {
        this.payment_time = payment_time;
    }
    
    public void setInfo_payment(String info_payment) {
        this.info_payment = info_payment;
    }
}
