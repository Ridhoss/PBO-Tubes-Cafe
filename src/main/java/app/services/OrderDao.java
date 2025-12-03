/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.services;

import dao.GenericDaoImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Cart;
import models.Order;

/**
 *
 * @author RIDHO
 */
public class OrderDao extends GenericDaoImpl<Integer, Order> {

    public OrderDao() {
        super(
                "orders",
                "order_id",
                "user_id",
                "total_amount",
                "discount",
                "final_amount",
                "payment_status",
                "order_status",
                "notes"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, Order o) throws Exception {
        ps.setInt(1, o.getUser_id());
        ps.setInt(2, o.getTotal_amount());
        ps.setInt(3, o.getDiscount() != null ? o.getDiscount() : 0);
        ps.setInt(4, o.getFinal_amount() != null ? o.getFinal_amount() : o.getTotal_amount());
        ps.setString(5, o.getPayment_status());
        ps.setString(6, o.getOrder_status());
        ps.setString(7, o.getNotes() != null ? o.getNotes() : "");
    }

    @Override
    protected void setIdParam(PreparedStatement ps, Order o, int index) throws Exception {
        ps.setInt(index, o.getOrder_id());
    }

    @Override
    protected Order mapResult(ResultSet rs) throws Exception {
        Order o = new Order();

        o.setOrder_id(rs.getInt("order_id"));
        o.setUser_id(rs.getInt("user_id"));
        o.setDiscount(rs.getInt("discount"));
        o.setFinal_amount(rs.getInt("final_amount"));
        o.setPayment_status(rs.getString("payment_status"));
        o.setOrder_status(rs.getString("order_status"));
        o.setNotes(rs.getString("notes"));

        o.setOrder_date(rs.getTimestamp("order_date") != null
                ? rs.getTimestamp("order_date").toLocalDateTime()
                : null);

        return o;
    }
}
