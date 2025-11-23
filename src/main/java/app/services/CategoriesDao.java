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

import models.Category;

/**
 *
 * @author RIDHO
 */
public class CategoriesDao extends GenericDaoImpl<Integer, Category> {

    public CategoriesDao() {
        super(
                "categories",
                "category_id",
                "parent_id",
                "category_name",
                "category_type"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, Category c) throws Exception {

        if (c.getParent_id() != null) {
            ps.setInt(1, c.getParent_id());
        } else {
            ps.setNull(1, java.sql.Types.INTEGER);
        }

        ps.setString(2, c.getCategory_name());
        ps.setString(3, c.getCategory_type());
    }

    @Override
    protected void setIdParam(PreparedStatement ps, Category c, int index) throws Exception {
        ps.setInt(index, c.getCategory_id());
    }

    @Override
    protected Category mapResult(ResultSet rs) throws Exception {
        Category c = new Category();

        c.setCategory_id(rs.getInt("category_id"));

        int parent = rs.getInt("parent_id");
        c.setParent_id(rs.wasNull() ? null : parent);

        c.setCategory_name(rs.getString("category_name"));
        c.setCategory_type(rs.getString("category_type"));

        c.setCreated_at(rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime() : null);

        return c;
    }

    public void deleteAll() throws Exception {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM categories")) {
            ps.executeUpdate();
        }
    }
}
