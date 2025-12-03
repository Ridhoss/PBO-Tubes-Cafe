/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.OrderDao;
import app.services.OrderItemsDao;
import java.util.ArrayList;
import java.util.List;
import models.OrderItems;

/**
 *
 * @author RIDHO
 */
public class OrderItemsController {

    private static OrderItemsController instance;

    private final OrderItemsDao orderItemsDao;

    private OrderItemsController() {
        orderItemsDao = new OrderItemsDao();
    }

    public static synchronized OrderItemsController getInstance() {
        if (instance == null) {
            instance = new OrderItemsController();
        }
        return instance;
    }

    public void addOrderItem(Integer orderId, Integer productId, Integer quantity,
            Integer originalPrice, Integer subtotal, String note) throws Exception {
        OrderItems item = new OrderItems();
        item.setOrder_id(orderId);
        item.setProduct_id(productId);
        item.setQuantity(quantity);
        item.setOriginal_price(originalPrice);
        item.setSubtotal(subtotal);
        item.setNote(note);

        orderItemsDao.insert(item);
    }

    public void updateOrderItem(Integer orderItemId, Integer orderId, Integer productId,
            Integer quantity, Integer originalPrice, Integer subtotal,
            String note) throws Exception {
        OrderItems existing = orderItemsDao.findById(orderItemId);

        if (existing == null) {
            throw new Exception("Order Item ID " + orderItemId + " tidak ditemukan");
        }

        existing.setOrder_id(orderId);
        existing.setProduct_id(productId);
        existing.setQuantity(quantity);
        existing.setOriginal_price(originalPrice);
        existing.setSubtotal(subtotal);
        existing.setNote(note);

        orderItemsDao.update(existing);
    }

    public void deleteOrderItem(Integer orderItemId) throws Exception {
        OrderItems existing = orderItemsDao.findById(orderItemId);
        if (existing == null) {
            throw new Exception("Order Item ID " + orderItemId + " tidak ditemukan");
        }
        orderItemsDao.delete(existing.getOrder_item_id());
    }

    public OrderItems findOrderItemById(Integer orderItemId) throws Exception {
        OrderItems item = orderItemsDao.findById(orderItemId);
        if (item == null) {
            throw new Exception("Order Item ID " + orderItemId + " tidak ditemukan");
        }
        return item;
    }

    public List<OrderItems> getAllOrderItems() throws Exception {
        return orderItemsDao.findAll();
    }

    public List<OrderItems> getItemsByOrderId(Integer orderId) throws Exception {
        List<OrderItems> all = orderItemsDao.findAll();
        List<OrderItems> result = new ArrayList<>();
        for (OrderItems item : all) {
            if (item.getOrder_id().equals(orderId)) {
                result.add(item);
            }
        }
        return result;
    }
}
