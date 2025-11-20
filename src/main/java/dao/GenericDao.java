/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;

/**
 *
 * @author RIDHO
 */
public interface GenericDao<ID, T> {

    void insert(T entity) throws Exception;

    void update(T entity) throws Exception;

    void delete(ID id) throws Exception;

    T findById(ID id) throws Exception;

    List<T> findAll() throws Exception;
}
