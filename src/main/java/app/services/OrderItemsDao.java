/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.services;

import dao.GenericDaoImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Order;
import models.OrderItems;

/**
 *
 * @author RIDHO
 */
public class OrderItemsDao extends GenericDaoImpl<Integer, OrderItems> {

    public OrderItemsDao() {
        super(
                "order_items",
                "order_item_id",
                "order_id",
                "product_id",
                "quantity",
                "original_price",
                "subtotal",
                "notes"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, OrderItems o) throws Exception {
        ps.setInt(1, o.getOrder_item_id());
        ps.setInt(2, o.getOrder_id());
        ps.setInt(3, o.getProduct_id());
        ps.setInt(4, o.getQuantity());
        ps.setInt(5, o.getOriginal_price());
        ps.setInt(6, o.getSubtotal());
        ps.setString(6, o.getNote());
    }

    @Override
    protected void setIdParam(PreparedStatement ps, OrderItems o, int index) throws Exception {
        ps.setInt(index, o.getOrder_item_id());
    }

    @Override
    protected OrderItems mapResult(ResultSet rs) throws Exception {
        OrderItems o = new OrderItems();

        o.setOrder_item_id(rs.getInt("order_items_id"));
        o.setOrder_id(rs.getInt("order_id"));
        o.setProduct_id(rs.getInt("product_id"));
        o.setQuantity(rs.getInt("quantity"));
        o.setOriginal_price(rs.getInt("original_price"));
        o.setSubtotal(rs.getInt("subtotal"));
        o.setNote(rs.getString("notes"));

        return o;
    }

}
