/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.CartDao;
import java.util.List;
import models.Cart;

/**
 *
 * @author Dell
 */
public class CartController {

    private CartDao cartDao = new CartDao();

    public void createCart(Integer userId) throws Exception {
        Cart cart = new Cart();
        cart.setUser_id(userId);
        cartDao.insert(cart);
    }

    public Cart getCartByUser(Integer userId) throws Exception {
        return cartDao.findByUserId(userId);
    }

    public void deleteCart(Integer cartId) throws Exception {
        Cart existing = cartDao.findById(cartId);

        if (existing == null) {
            throw new Exception("Cart ID " + cartId + " tidak ditemukan");
        }

        cartDao.delete(cartId);
    }

    public List<Cart> getAllCarts() throws Exception {
        return cartDao.findAll();
    }
}
