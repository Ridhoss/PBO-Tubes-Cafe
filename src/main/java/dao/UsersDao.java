/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import database.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import models.User;

/**
 *
 * @author RIDHO
 */
public class UsersDao extends GenericDaoImpl<Integer, User> {

    public UsersDao() {
        super(
                "users",
                "iduser",
                "name", "email", "password"
        );
    }

    @Override
    protected void setParams(PreparedStatement ps, User u) throws Exception {
        ps.setString(1, u.getName());
        ps.setString(2, u.getEmail());
        ps.setString(3, u.getPassword());
    }

    @Override
    protected void setIdParam(PreparedStatement ps, User u, int index) throws Exception {
        ps.setInt(index, u.getId());
    }

    @Override
    protected User mapResult(ResultSet rs) throws Exception {
        return new User(
                rs.getInt("iduser"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        );
    }

    public User findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new User(
                    rs.getInt("iduser"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            );
        }
        return null;
    }

}
