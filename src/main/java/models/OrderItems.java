/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author RIDHO
 */
public class OrderItems {

    private Integer order_item_id;
    private Integer order_id;
    private Integer product_id;
    private Integer quantity;
    private Integer original_price;
    private Integer subtotal;
    private String note;

    public OrderItems() {
    }

    public OrderItems(Integer order_item_id, Integer order_id, Integer product_id, Integer quantity,
            Integer original_price, Integer subtotal, String note) {
        this.order_item_id = order_item_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.original_price = original_price;
        this.subtotal = subtotal;
        this.note = note;
    }

    public Integer getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(Integer order_item_id) {
        this.order_item_id = order_item_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(Integer original_price) {
        this.original_price = original_price;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
