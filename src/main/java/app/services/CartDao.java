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
import models.Cart;

/**
 *
 * @author Dell
 */
public class CartDao extends GenericDaoImpl<Integer, Cart> {

    public CartDao() {
        super(
                "cart",
                "cart_id",
                "user_id",
                "created_at",
                "updated_at"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, Cart c) throws Exception {

        ps.setInt(1, c.getUser_id());

        if (c.getCreated_at() != null) {
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(c.getCreated_at()));
        } else {
            ps.setNull(2, java.sql.Types.TIMESTAMP);
        }

        if (c.getUpdated_at() != null) {
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(c.getUpdated_at()));
        } else {
            ps.setNull(3, java.sql.Types.TIMESTAMP);
        }
    }

    @Override
    protected void setIdParam(PreparedStatement ps, Cart c, int index) throws Exception {
        ps.setInt(index, c.getCart_id());
    }

    @Override
    protected Cart mapResult(ResultSet rs) throws Exception {
        Cart c = new Cart();

        c.setCart_id(rs.getInt("cart_id"));
        c.setUser_id(rs.getInt("user_id"));

        c.setCreated_at(rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime()
                : null);

        c.setUpdated_at(rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime()
                : null);

        return c;
    }

    public List<Cart> findAll() throws Exception {
        List<Cart> list = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT * FROM cart ORDER BY cart_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResult(rs));
            }
        }
        return list;
    }
}
