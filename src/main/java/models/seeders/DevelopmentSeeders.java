/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.seeders;

import app.controller.AuthController;
import app.services.CategoriesDao;
import app.services.ProductsDao;
import app.services.UsersDao;
import models.User;
import models.Category;
import models.Product;
import util.PasswordUtil;

/**
 *
 * @author RIDHO
 */
public class DevelopmentSeeders {

    public static void main(String[] args) {
        try {
//            seeder user
            UsersDao userDao = new UsersDao();

            userDao.deleteAll();

            Object[][] users = {
                {"customer", "password123", "Customer", "customer@gmail.com", "08123456789", "customer"},
                {"admin", "password123", "Admin", "admin@gmail.com", "0812343216789", "admin"},
                {"cashier", "password123", "Cashier", "cashier@gmail.com", "0812345632789", "cashier"}
            };

            for (Object[] data : users) {

                User u = new User();
                u.setUsername((String) data[0]);
                u.setPassword(PasswordUtil.hash((String) data[1]));
                u.setFull_name((String) data[2]);
                u.setEmail((String) data[3]);
                u.setPhone_number((String) data[4]);
                u.setRole((String) data[5]);
                u.setIs_active(true);

                userDao.insert(u);
            }

//            seeder category
            CategoriesDao categoryDao = new CategoriesDao();
            categoryDao.deleteAll();

            Object[][] categories = {
                {null, "Food", "food"},
                {null, "Drink", "drink"},
                {null, "Dessert", "dessert"},
                {1, "Main Course", "food"},
                {1, "Snacks", "food"},
                {2, "Coffee", "drink"},
                {2, "Juice", "drink"},
                {3, "Cake", "dessert"}
            };

            for (Object[] data : categories) {
                Category c = new Category();

                c.setParent_id((Integer) data[0]);
                c.setCategory_name((String) data[1]);
                c.setCategory_type((String) data[2]);

                categoryDao.insert(c);
            }

//            seeder product
            ProductsDao productDao = new ProductsDao();
            productDao.deleteAll();

            Object[][] products = {
                {4, "Nasi Goreng Spesial", "Nasi goreng dengan telur dan ayam", 25000, 15000, 20, true, null},
                {4, "Ayam Geprek", "Ayam crispy dengan sambal geprek", 22000, 12000, 15, true, null},
                {5, "Kentang Goreng", "French fries dengan saus", 15000, 8000, 30, true, null},
                {5, "Risoles Mayo", "Risoles isi mayo dan smoked beef", 12000, 6000, 25, true, null},
                {6, "Kopi Latte", "Kopi susu latte panas", 20000, 9000, 10, true, null},
                {6, "Es Kopi Susu", "Kopi susu gula aren", 18000, 8000, 18, true, null},
                {7, "Jus Alpukat", "Alpukat segar tanpa gula", 17000, 9000, 12, true, null},
                {7, "Jus Jeruk", "Jeruk peras asli", 15000, 7000, 20, true, null},
                {8, "Cheesecake", "Kue keju lembut", 28000, 16000, 8, true, null},
                {8, "Chocolate Brownies", "Brownies coklat premium", 25000, 14000, 10, true, null}
            };

            for (Object[] data : products) {
                Product p = new Product();

                p.setCategory_id((Integer) data[0]);
                p.setProduct_name((String) data[1]);
                p.setDescription((String) data[2]);
                p.setPrice((Integer) data[3]);
                p.setCost_price((Integer) data[4]);
                p.setStock((Integer) data[5]);
                p.setIs_active((Boolean) data[6]);
                p.setImage_path((String) data[7]);

                productDao.insert(p);
            }

            System.out.println("Seeder completed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
