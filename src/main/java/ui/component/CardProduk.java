/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.component;

import app.controller.CartController;
import app.controller.CartItemsController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import models.Cart;
import models.Product;

/**
 *
 * @author Dell
 */
public class CardProduk extends JPanel {

    public CardProduk(Product p) {
        setPreferredSize(new Dimension(200, 260));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setLayout(new BorderLayout());

        JLabel img = new JLabel("No Image", SwingConstants.CENTER);
        img.setPreferredSize(new Dimension(200, 140));

        try {
            if (p.getImage_path() != null) {
                String path = p.getImage_path();
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }

                ImageIcon icon = new ImageIcon(getClass().getResource(path));
                Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                img.setIcon(new ImageIcon(scaled));
                img.setText(null);
            }
        } catch (Exception e) {
            System.out.println("Image load failed: " + p.getImage_path());
        }

        JLabel name = new JLabel(p.getProduct_name());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel price = new JLabel("Rp " + String.format("%,d", p.getPrice()));
        price.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnAdd = new JButton("Add To Cart");
        btnAdd.addActionListener(evt -> addToCart(p));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(name);
        info.add(price);
        info.add(btnAdd);

        add(img, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
    }

    private void addToCart(Product p) {
        try {
            CartController cartController = new CartController();
            CartItemsController itemController = new CartItemsController();

            Cart cart = cartController.getCartByUser(1); 

            if (cart == null) {
                cartController.createCart(1);
                cart = cartController.getCartByUser(1);
            }

            itemController.addItem(cart.getCart_id(), p.getProduct_id(), 1);

            JOptionPane.showMessageDialog(this, "Produk ditambahkan!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
