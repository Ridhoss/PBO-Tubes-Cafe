/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.OrderDao;
import app.services.OrderItemsDao;

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
}
