/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RIDHO
 */
public abstract class GenericDaoImpl<ID, T> implements GenericDao<ID, T> {

    protected final String table;              // nama tabel
    protected final String idColumn;           // kolom id
    protected final String[] columns;          // kolom selain id

    public GenericDaoImpl(String table, String idColumn, String... columns) {
        this.table = table;
        this.idColumn = idColumn;
        this.columns = columns;
    }

    // =============== INSERT ===============
    @Override
    public void insert(T entity) throws Exception {
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");

        for (String col : columns) {
            sql.append(col).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") VALUES (");

        sql.append("?,".repeat(columns.length));
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {

            setParams(ps, entity); // mapping entity → params
            ps.executeUpdate();
        }
    }

    // =============== UPDATE ===============
    @Override
    public void update(T entity) throws Exception {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");

        for (String col : columns) {
            sql.append(col).append("=?,");
        }
        sql.deleteCharAt(sql.length() - 1);

        sql.append(" WHERE ").append(idColumn).append("=?");

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            setParams(ps, entity);
            setIdParam(ps, entity, columns.length + 1);
            ps.executeUpdate();
        }
    }

    // =============== DELETE ===============
    @Override
    public void delete(ID id) throws Exception {
        String sql = "DELETE FROM " + table + " WHERE " + idColumn + "=?";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    // =============== FIND BY ID ===============
    @Override
    public T findById(ID id) throws Exception {
        String sql = "SELECT * FROM " + table + " WHERE " + idColumn + "=?";
        
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, id);

            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapResult(rs) : null;
        }
    }

    // =============== FIND ALL ===============
    @Override
    public List<T> findAll() throws Exception {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + table;

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResult(rs));
            }
        }
        return list;
    }

    // ============= ABSTRACT METHOD =============
    // Cara mengisi params insert/update
    protected abstract void setParams(PreparedStatement ps, T entity) throws Exception;

    // Cara set id pada update
    protected abstract void setIdParam(PreparedStatement ps, T entity, int index) throws Exception;

    // Cara mapping ResultSet → object
    protected abstract T mapResult(ResultSet rs) throws Exception;
}
