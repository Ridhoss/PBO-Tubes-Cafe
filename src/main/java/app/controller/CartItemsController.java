/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.CartItemsDao;
import java.util.List;
import models.CartItem;

/**
 *
 * @author Dell
 */
public class CartItemsController {

    private CartItemsDao cartItemsDao = new CartItemsDao();

    public void addItem(Integer cartId, Integer productId, Integer quantity) throws Exception {
        CartItem item = cartItemsDao.findByCartAndProduct(cartId, productId);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            cartItemsDao.update(item);
            return;
        }

        CartItem newItem = new CartItem();
        newItem.setCart_id(cartId);
        newItem.setProduct_id(productId);
        newItem.setQuantity(quantity);

        cartItemsDao.insert(newItem);
    }

    public void updateQuantity(Integer cartItemId, Integer newQty) throws Exception {
        CartItem existing = cartItemsDao.findById(cartItemId);

        if (existing == null) {
            throw new Exception("Cart Item ID " + cartItemId + " tidak ditemukan");
        }

        existing.setQuantity(newQty);
        cartItemsDao.update(existing);
    }

    public void removeItem(Integer cartItemId) throws Exception {
        CartItem item = cartItemsDao.findById(cartItemId);

        if (item == null) {
            throw new Exception("Cart Item ID " + cartItemId + " tidak ditemukan");
        }

        cartItemsDao.delete(cartItemId);
    }

    public List<CartItem> getItemsByCart(Integer cartId) throws Exception {
        return cartItemsDao.findByCartId(cartId);
    }
}
