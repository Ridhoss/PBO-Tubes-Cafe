/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.UsersDao;
import models.User;
import util.PasswordUtil;

/**
 *
 * @author RIDHO
 */
public class AuthController {

    // === Singleton Instance ===
    private static AuthController instance;

    // === DAO ===
    private final UsersDao userDao;

    // === Private constructor ===
    private AuthController() {
        userDao = new UsersDao();
    }

    public static synchronized AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    // === Register ===
    public void register(String username, String password, String fullName,
            String email, String phone) throws Exception {

        if (userDao.findByUsername(username) != null) {
            throw new Exception("Username sudah digunakan");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(PasswordUtil.hash(password));
        u.setFull_name(fullName);
        u.setEmail(email);
        u.setPhone_number(phone);
        u.setRole("customer");
        u.setIs_active(true);

        userDao.insert(u);
    }

    // === Login ===
    public User login(String username, String password) throws Exception {

        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new Exception("Username tidak ditemukan");
        }
        if (!user.getIs_active()) {
            throw new Exception("Akun ini tidak aktif");
        }

        String hashedInputPassword = PasswordUtil.hash(password);

        if (!hashedInputPassword.equals(user.getPassword())) {
            throw new Exception("Password salah");
        }

        return user;
    }
}
