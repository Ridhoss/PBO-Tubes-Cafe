/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.services.ProductsDao;
import app.services.UsersDao;
import java.util.List;
import models.User;
import util.PasswordUtil;

/**
 *
 * @author RIDHO
 */
public class UserController {

    private static UserController instance;

    private final UsersDao userDao;

    private UserController() {
        userDao = new UsersDao();
    }

    public static synchronized UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public void addUser(
            String username,
            String password,
            String fullName,
            String email,
            String phone,
            String role,
            Boolean isActive
    ) throws Exception {

        if (userDao.findByUsername(username) != null) {
            throw new Exception("Username sudah digunakan");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(PasswordUtil.hash(password));
        u.setFull_name(fullName);
        u.setEmail(email);
        u.setPhone_number(phone);
        u.setRole(role);
        u.setIs_active(isActive);

        userDao.insert(u);
    }

    public void updateUser(
            Integer userId,
            String username,
            String password,
            String fullName,
            String email,
            String phone,
            String role,
            Boolean isActive
    ) throws Exception {

        User existing = userDao.findById(userId);
        if (existing == null) {
            throw new Exception("User ID " + userId + " tidak ditemukan");
        }

        User checkUsername = userDao.findByUsername(username);
        if (checkUsername != null && !checkUsername.getUser_id().equals(userId)) {
            throw new Exception("Username sudah dipakai user lain");
        }

        existing.setUsername(username);
        existing.setPassword(PasswordUtil.hash(password));
        existing.setFull_name(fullName);
        existing.setEmail(email);
        existing.setPhone_number(phone);
        existing.setRole(role);
        existing.setIs_active(isActive);

        userDao.update(existing);
    }

    public void updateUserWithouPass(
            Integer userId,
            String username,
            String fullName,
            String email,
            String phone,
            String role,
            Boolean isActive
    ) throws Exception {

        User existing = userDao.findById(userId);
        if (existing == null) {
            throw new Exception("User ID " + userId + " tidak ditemukan");
        }

        User checkUsername = userDao.findByUsername(username);
        if (checkUsername != null && !checkUsername.getUser_id().equals(userId)) {
            throw new Exception("Username sudah dipakai user lain");
        }

        existing.setUsername(username);
        existing.setFull_name(fullName);
        existing.setEmail(email);
        existing.setPhone_number(phone);
        existing.setRole(role);
        existing.setIs_active(isActive);

        userDao.update(existing);
    }

    public void deleteUser(Integer userId) throws Exception {
        User u = userDao.findById(userId);

        if (u == null) {
            throw new Exception("User ID " + userId + " tidak ditemukan");
        }

        userDao.delete(userId);
    }

    public User findById(Integer id) throws Exception {
        return userDao.findById(id);
    }

    public User findByUsername(String username) throws Exception {
        return userDao.findByUsername(username);
    }

    public List<User> getAllUsers() throws Exception {
        return userDao.findAll();
    }

}
