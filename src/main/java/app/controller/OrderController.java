/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.CategoriesDao;
import app.services.OrderDao;
import java.util.List;
import models.Order;

/**
 *
 * @author RIDHO
 */
public class OrderController {

    private static OrderController instance;

    private final OrderDao orderDao;

    private OrderController() {
        orderDao = new OrderDao();
    }

    public static synchronized OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }

    public void addOrder(Integer userId, Integer totalAmount, Integer discount,
            Integer finalAmount, String paymentStatus, String orderStatus, String notes) throws Exception {
        Order o = new Order();
        o.setUser_id(userId);
        o.setTotal_amount(totalAmount);
        o.setDiscount(discount);
        o.setFinal_amount(finalAmount);
        o.setPayment_status(paymentStatus);
        o.setOrder_status(orderStatus);
        o.setNotes(notes);

        orderDao.insert(o);
    }

    public void updateOrder(Integer orderId, Integer userId, Integer totalAmount,
            Integer discount, Integer finalAmount, String paymentStatus,
            String orderStatus, String notes) throws Exception {
        Order existing = orderDao.findById(orderId);

        if (existing == null) {
            throw new Exception("Order ID " + orderId + " tidak ditemukan");
        }

        existing.setUser_id(userId);
        existing.setTotal_amount(totalAmount);
        existing.setDiscount(discount);
        existing.setFinal_amount(finalAmount);
        existing.setPayment_status(paymentStatus);
        existing.setOrder_status(orderStatus);
        existing.setNotes(notes);

        orderDao.update(existing);
    }

    public void deleteOrder(Integer orderId) throws Exception {
        Order existing = orderDao.findById(orderId);
        if (existing == null) {
            throw new Exception("Order ID " + orderId + " tidak ditemukan");
        }
        orderDao.delete(existing.getOrder_id());
    }

    public Order findOrderById(Integer orderId) throws Exception {
        Order o = orderDao.findById(orderId);
        if (o == null) {
            throw new Exception("Order ID " + orderId + " tidak ditemukan");
        }
        return o;
    }

    public List<Order> getAllOrders() throws Exception {
        return orderDao.findAll();
    }

    public List<Order> getOrdersByUserId(Integer userId) throws Exception {
        List<Order> all = orderDao.findAll();
        List<Order> result = new java.util.ArrayList<>();
        for (Order o : all) {
            if (o.getUser_id().equals(userId)) {
                result.add(o);
            }
        }
        return result;
    }
}
