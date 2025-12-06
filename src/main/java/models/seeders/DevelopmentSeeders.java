/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.seeders;

import app.controller.AuthController;
import app.services.CategoriesDao;
import app.services.ProductsDao;
import app.services.UsersDao;
import database.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
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
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();

            st.execute("ALTER TABLE products DISABLE TRIGGER ALL");
            st.execute("ALTER TABLE categories DISABLE TRIGGER ALL");
            st.execute("ALTER TABLE users DISABLE TRIGGER ALL");

            st.execute("TRUNCATE TABLE products RESTART IDENTITY CASCADE");
            st.execute("TRUNCATE TABLE categories RESTART IDENTITY CASCADE");
            st.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");

            st.execute("ALTER TABLE products ENABLE TRIGGER ALL");
            st.execute("ALTER TABLE categories ENABLE TRIGGER ALL");
            st.execute("ALTER TABLE users ENABLE TRIGGER ALL");

//            seeder user
            UsersDao userDao = new UsersDao();

            userDao.deleteAll();

            Object[][] users = {
                {"customer", "123", "Customer", "customer@gmail.com", "08123456789", "customer"},
                {"admin", "123", "Admin", "admin@gmail.com", "0812343216789", "admin"},
                {"cashier", "123", "Cashier", "cashier@gmail.com", "0812345632789", "cashier"}
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
                {null, "Food", "food", "categoryimages/icon_food.png"},
                {null, "Drink", "drink", "categoryimages/icon_drink.png"},
                {null, "Dessert", "dessert", "categoryimages/icon_dessert.png"},
                {1, "Main Course", "food", "categoryimages/icon_maincourse.png"},
                {1, "Snacks", "food", "categoryimages/icon_snack.png"},
                {2, "Coffee", "drink", "categoryimages/icon_coffe.png"},
                {2, "Juice", "drink", "categoryimages/icon_juice.png"},
                {3, "Cake", "dessert", "categoryimages/icon_cake.png"}
            };

            for (Object[] data : categories) {
                Category c = new Category();

                c.setParent_id((Integer) data[0]);
                c.setCategory_name((String) data[1]);
                c.setCategory_type((String) data[2]);
                c.setImage_path((String) data[3]);

                categoryDao.insert(c);
            }

//            seeder product
            ProductsDao productDao = new ProductsDao();
            productDao.deleteAll();

            Object[][] products = {
                {4, "Nasi Goreng Spesial", "Nasi goreng dengan telur dan ayam", 25000, 15000, 20, true, "productimages/NasiGoreng.jpg"},
                {4, "Ayam Geprek", "Ayam crispy dengan sambal geprek", 22000, 12000, 15, true, "productimages/AyamGeprek.jpg"},
                {5, "Kentang Goreng", "French fries dengan saus", 15000, 8000, 30, true, "productimages/KentangGoreng.jpg"},
                {5, "Risoles Mayo", "Risoles isi mayo dan smoked beef", 12000, 6000, 25, true, "productimages/RisolesMayo.jpg"},
                {6, "Kopi Latte", "Kopi susu latte panas", 20000, 9000, 10, true, "productimages/KopiLatte.jpg"},
                {6, "Es Kopi Susu", "Kopi susu gula aren", 18000, 8000, 18, true, "productimages/EsKopisusu.jpg"},
                {7, "Jus Alpukat", "Alpukat segar tanpa gula", 17000, 9000, 12, true, "productimages/JusAlpukat.jpg"},
                {7, "Jus Jeruk", "Jeruk peras asli", 15000, 7000, 20, true, "productimages/JusJeruk.jpg"},
                {8, "Cheesecake", "Kue keju lembut", 28000, 16000, 8, true, "productimages/cheese.jpg"},
                {8, "Chocolate Brownies", "Brownies coklat premium", 25000, 14000, 10, true, "productimages/Brownies.jpg"}
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
