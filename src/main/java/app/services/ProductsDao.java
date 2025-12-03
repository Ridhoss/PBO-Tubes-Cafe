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
                "image_path");
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
                ? rs.getTimestamp("created_at").toLocalDateTime()
                : null);

        p.setUpdated_at(rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime()
                : null);

        return p;
    }

    public void deleteAll() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM products")) {
            ps.executeUpdate();
        }
    }

    public Product findById(Integer id) throws Exception {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        }

        return null;
    }

    public Product findByName(String name) throws Exception {
        String sql = "SELECT * FROM products WHERE LOWER(product_name) = LOWER(?)";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        }

        return null;
    }

    public java.util.List<Product> getAll() throws Exception {
        java.util.List<Product> list = new java.util.ArrayList<>();

        String sql = "SELECT * FROM products ORDER BY product_id ASC";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResult(rs));
            }

        }

        return list;
    }

    public List<Product> findByCategoryList(List<Integer> ids) throws Exception {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String inSql = String.join(",", ids.stream().map(id -> "?").toList());

        String sql = "SELECT * FROM products WHERE category_id IN (" + inSql + ")";

        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < ids.size(); i++) {
            ps.setInt(i + 1, ids.get(i));
        }

        ResultSet rs = ps.executeQuery();

        List<Product> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResult(rs));
        }

        return list;
    }

}
