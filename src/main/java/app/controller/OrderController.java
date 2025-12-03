/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.CategoriesDao;
import app.services.OrderDao;

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

}
