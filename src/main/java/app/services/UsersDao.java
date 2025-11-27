/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.services;

import dao.GenericDaoImpl;
import database.DBConnection;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import models.User;

/**
 *
 * @author RIDHO
 */
public class UsersDao extends GenericDaoImpl<Integer, User> {

    public UsersDao() {
        super(
                "users",
                "user_id",
                "username",
                "password",
                "full_name",
                "email",
                "phone_number",
                "role",
                "is_active"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, User u) throws Exception {
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPassword());
        ps.setString(3, u.getFull_name());
        ps.setString(4, u.getEmail());
        ps.setString(5, u.getPhone_number());
        ps.setString(6, u.getRole());
        ps.setBoolean(7, u.getIs_active());
    }

    @Override
    protected void setIdParam(PreparedStatement ps, User u, int index) throws Exception {
        ps.setInt(index, u.getUser_id());
    }

    @Override
    protected User mapResult(ResultSet rs) throws Exception {
        User user = new User();

        user.setUser_id(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFull_name(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone_number(rs.getString("phone_number"));
        user.setRole(rs.getString("role"));
        user.setIs_active(rs.getBoolean("is_active"));

        user.setCreated_at(rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime()
                : null);

        user.setUpdated_at(rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime()
                : null);

        return user;
    }

    public User findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ? LIMIT 1";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResult(rs);
            }
        }
        return null;
    }

    public void deleteAll() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM users")) {
            ps.executeUpdate();
        }
    }
}
