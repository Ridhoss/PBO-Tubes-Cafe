/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.CartItemsDao;
import app.services.UsersDao;
import java.util.List;
import models.CartItem;

/**
 *
 * @author Dell
 */
public class CartItemsController {

    // === Singleton Instance ===
    private static CartItemsController instance;

    // === DAO ===
    private final CartItemsDao cartItemsDao;

    // === Private constructor ===
    private CartItemsController() {
        cartItemsDao = new CartItemsDao();
    }

    public static synchronized CartItemsController getInstance() {
        if (instance == null) {
            instance = new CartItemsController();
        }
        return instance;
    }

    public void insertCartItem(Integer cartId, Integer productId, Integer quantity) throws Exception {
        try {
            CartItem existing = cartItemsDao.findByCartAndProduct(cartId, productId);
            if (existing != null) {
                // Kalau ada, tambahkan quantity
                existing.setQuantity(existing.getQuantity() + quantity);
                cartItemsDao.update(existing);
            } else {
                CartItem newItem = new CartItem();
                newItem.setCart_id(cartId);
                newItem.setProduct_id(productId);
                newItem.setQuantity(quantity);
                cartItemsDao.insert(newItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Gagal menambahkan item ke cart: " + e.getMessage());
        }
    }

    public void updateCartItem(Integer cartItemId, Integer newQty) throws Exception {
        try {
            CartItem existing = cartItemsDao.findById(cartItemId);
            if (existing == null) {
                throw new Exception("Cart Item ID " + cartItemId + " tidak ditemukan");
            }
            existing.setQuantity(newQty);
            cartItemsDao.update(existing);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Gagal update cart item: " + e.getMessage());
        }
    }

    public void deleteCartItem(Integer cartItemId) throws Exception {
        try {
            CartItem existing = cartItemsDao.findById(cartItemId);
            if (existing == null) {
                throw new Exception("Cart Item ID " + cartItemId + " tidak ditemukan");
            }
            cartItemsDao.delete(cartItemId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Gagal menghapus cart item: " + e.getMessage());
        }
    }

    public List<CartItem> getItemsByCart(Integer cartId) throws Exception {
        try {
            return cartItemsDao.findByCartId(cartId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Gagal mengambil item cart: " + e.getMessage());
        }
    }
}
