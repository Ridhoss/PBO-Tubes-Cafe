/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.services;

import dao.GenericDaoImpl;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.CartItem;

/**
 *
 * @author Dell
 */
public class CartItemsDao extends GenericDaoImpl<Integer, CartItem> {

    public CartItemsDao() {
        super(
                "cart_items",
                "cart_item_id",
                "cart_id",
                "product_id",
                "quantity",
                "added_at"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, CartItem c) throws Exception {

        ps.setInt(1, c.getCart_id());
        ps.setInt(2, c.getProduct_id());
        ps.setInt(3, c.getQuantity());

        if (c.getAdded_at() != null) {
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(c.getAdded_at()));
        } else {
            ps.setNull(4, java.sql.Types.TIMESTAMP);
        }
    }

    @Override
    protected void setIdParam(PreparedStatement ps, CartItem c, int index) throws Exception {
        ps.setInt(index, c.getCart_item_id());
    }

    @Override
    protected CartItem mapResult(ResultSet rs) throws Exception {
        CartItem item = new CartItem();

        item.setCart_item_id(rs.getInt("cart_item_id"));
        item.setCart_id(rs.getInt("cart_id"));
        item.setProduct_id(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));

        item.setAdded_at(rs.getTimestamp("added_at") != null
                ? rs.getTimestamp("added_at").toLocalDateTime()
                : null);

        return item;
    }

    public List<CartItem> findAll() throws Exception {
        List<CartItem> list = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT * FROM cart_items ORDER BY cart_item_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResult(rs));
            }
        }
        return list;
    }

    public List<CartItem> findByCartId(Integer cartId) throws Exception {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM cart_items WHERE cart_id = ? ORDER BY cart_item_id ASC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResult(rs));
                }
            }
        }
        return items;
    }

    public CartItem findByCartAndProduct(Integer cartId, Integer productId) throws Exception {
        CartItem item = null;
        String sql = "SELECT * FROM cart_items WHERE cart_id = ? AND product_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item = mapResult(rs);
                }
            }
        }
        return item;
    }

}
