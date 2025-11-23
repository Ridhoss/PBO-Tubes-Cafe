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

import models.Product;

/**
 *
 * @author RIDHO
 */
public class ProductsDao extends GenericDaoImpl<Integer, Product> {

    public ProductsDao() {
        super(
                "products",
                "product_id",
                "category_id",
                "product_name",
                "description",
                "price",
                "cost_price",
                "stock",
                "is_active",
                "image_path"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, Product p) throws Exception {
        ps.setInt(1, p.getCategory_id());
        ps.setString(2, p.getProduct_name());
        ps.setString(3, p.getDescription());
        ps.setInt(4, p.getPrice());
        ps.setInt(5, p.getCost_price());
        ps.setInt(6, p.getStock());
        ps.setBoolean(7, p.getIs_active());
        ps.setString(8, p.getImage_path());
    }

    @Override
    protected void setIdParam(PreparedStatement ps, Product p, int index) throws Exception {
        ps.setInt(index, p.getProduct_id());
    }

    @Override
    protected Product mapResult(ResultSet rs) throws Exception {
        Product p = new Product();

        p.setProduct_id(rs.getInt("product_id"));
        p.setCategory_id(rs.getInt("category_id"));
        p.setProduct_name(rs.getString("product_name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getInt("price"));
        p.setCost_price(rs.getInt("cost_price"));
        p.setStock(rs.getInt("stock"));
        p.setIs_active(rs.getBoolean("is_active"));
        p.setImage_path(rs.getString("image_path"));

        p.setCreated_at(rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime() : null);

        p.setUpdated_at(rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime() : null);

        return p;
    }

    public void deleteAll() throws Exception {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM products")) {
            ps.executeUpdate();
        }
    }
}
