/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import models.Category;
import app.services.CategoriesDao;

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
}
