package main;

import app.controller.AuthController;
import models.User;
import app.services.UsersDao;
import ui.KF;
import ui.layout.LayoutAdmin;
import ui.Login;
import util.PasswordUtil;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author RIDHO
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KF.flogin.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        KF.flogin.setVisible(true);
    }
}
