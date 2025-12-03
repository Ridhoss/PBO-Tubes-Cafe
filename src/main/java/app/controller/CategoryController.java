/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import models.Category;
import app.services.CategoriesDao;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RIDHO
 */
public class CategoryController {

    CategoriesDao categoryDao = new CategoriesDao();

    public void addCategory(Integer parentId, String name, String type) throws Exception {
        Category c = new Category();

        c.setParent_id(parentId);
        c.setCategory_name(name);
        c.setCategory_type(type);

        categoryDao.insert(c);
    }

    public void updateCategory(Integer categoryId, Integer parentId, String name, String type) throws Exception {

        Category existing = categoryDao.findById(categoryId);

        if (existing == null) {
            throw new Exception("Category ID " + categoryId + " tidak ditemukan");
        }

        existing.setParent_id(parentId);
        existing.setCategory_name(name);
        existing.setCategory_type(type);

        categoryDao.update(existing);
    }

    public void deleteCategory(Integer categoryId) throws Exception {

        Category c = categoryDao.findById(categoryId);

        if (c == null) {
            throw new Exception("Category ID " + categoryId + " tidak ditemukan");
        }

        categoryDao.delete(c.getCategory_id());
    }

    public List<Category> getAllCategories() throws Exception {
        return categoryDao.findAll();
    }

    public List<Category> getParentCategories() throws Exception {
        List<Category> allCategories = categoryDao.findAll();
        List<Category> parentCategories = new java.util.ArrayList<>();

        for (Category c : allCategories) {
            if (c.getParent_id() == null) {
                parentCategories.add(c);
            }
        }

        return parentCategories;
    }

    public List<Integer> getAllChildIds(int parentId) throws Exception {
        List<Category> all = categoryDao.findAll();
        List<Integer> result = new ArrayList<>();
        findChildren(parentId, all, result);
        return result;
    }

    private void findChildren(int parentId, List<Category> all, List<Integer> result) {
        for (Category c : all) {
            if (c.getParent_id() != null && c.getParent_id() == parentId) {
                result.add(c.getCategory_id());
                findChildren(c.getCategory_id(), all, result);
            }
        }
    }

}
