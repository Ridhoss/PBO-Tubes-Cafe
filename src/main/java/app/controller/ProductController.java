/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import models.Product;
import app.services.ProductsDao;

/**
 *
 * @author RIDHO
 */
public class ProductController {

    private static ProductController instance;

    private final ProductsDao productDao;

    private ProductController() {
        productDao = new ProductsDao();
    }
    
    public static synchronized ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }

    // === Methods ===
    public void addProduct(
            Integer categoryId,
            String productName,
            String description,
            Integer price,
            Integer costPrice,
            Integer stock,
            Boolean isActive,
            String imagePath
    ) throws Exception {

        Product p = new Product();

        p.setCategory_id(categoryId);
        p.setProduct_name(productName);
        p.setDescription(description);
        p.setPrice(price);
        p.setCost_price(costPrice);
        p.setStock(stock);
        p.setIs_active(isActive);
        p.setImage_path(imagePath);

        productDao.insert(p);
    }

    public void updateProduct(
            Integer productId,
            Integer categoryId,
            String productName,
            String description,
            Integer price,
            Integer costPrice,
            Integer stock,
            Boolean isActive,
            String imagePath
    ) throws Exception {

        Product existing = productDao.findById(productId);

        if (existing == null) {
            throw new Exception("Product ID " + productId + " tidak ditemukan");
        }

        existing.setCategory_id(categoryId);
        existing.setProduct_name(productName);
        existing.setDescription(description);
        existing.setPrice(price);
        existing.setCost_price(costPrice);
        existing.setStock(stock);
        existing.setIs_active(isActive);
        existing.setImage_path(imagePath);

        productDao.update(existing);
    }

    public void deleteProduct(Integer productId) throws Exception {
        Product p = productDao.findById(productId);

        if (p == null) {
            throw new Exception("Product ID " + productId + " tidak ditemukan");
        }

        productDao.delete(p.getProduct_id());
    }
}
