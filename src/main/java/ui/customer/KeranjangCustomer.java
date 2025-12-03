/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.customer;

import app.controller.CartController;
import app.controller.CartItemsController;
import app.services.ProductsDao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import models.Cart;
import models.CartItem;
import models.Product;

/**
 *
 * @author Dell
 */
public class KeranjangCustomer extends javax.swing.JPanel {

    /**
     * Creates new form KeranjangCustomer
     */
    private int userId;
    private int cartId;

    private CartController cartController = new CartController();
    private CartItemsController cartItemsController = new CartItemsController();
    private ProductsDao productDao = new ProductsDao();

    private static final java.util.logging.Logger logger
            = java.util.logging.Logger.getLogger(KeranjangCustomer.class.getName());

    public KeranjangCustomer(Integer userId1) {
        if (userId1 == null || userId1 <= 0) {
            throw new IllegalArgumentException("User ID harus valid (lebih dari 0)");
        }

        this.userId = userId1;
        logger.log(java.util.logging.Level.INFO, "Initializing cart for user: {0}", userId);

        initComponents();
        loadCartData();
    }

    private void loadCartData() {
        try {
            Cart cart = cartController.getCartByUser(userId);

            if (cart == null) {
                cartController.createCart(userId);
                cart = cartController.getCartByUser(userId);
            }

            cartId = cart.getCart_id();

            List<CartItem> items = cartItemsController.getItemsByCart(cartId);

            showItems(items);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat keranjang: " + e.getMessage());
        }
    }

    private void showItems(List<CartItem> items) throws Exception {

        pnlkeranjang1.removeAll();
 

        JPanel[] panels = {pnlkeranjang1};

        int index = 0;
        for (CartItem ci : items) {

            if (index >= 3) {
                break; 
            }
            Product p = productDao.findById(ci.getProduct_id());

            JPanel card = createCartCard(ci, p);

            panels[index].add(card, BorderLayout.CENTER);

            index++;
        }

        revalidate();
        repaint();
    }

    private JPanel createCartCard(CartItem item, Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setPreferredSize(new Dimension(600, 150));

        JLabel img = new JLabel();
        img.setPreferredSize(new Dimension(120, 120));

        try {
            if (p.getImage_path() != null) {
                String path = p.getImage_path();
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }

                ImageIcon icon = new ImageIcon(getClass().getResource(path));
                Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                img.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) {
            img.setText("No Image");
            logger.log(java.util.logging.Level.WARNING, "Failed to load image: {0}", p.getImage_path());
        }

        JLabel name = new JLabel(p.getProduct_name());
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel price = new JLabel("Rp " + String.format("%,d", p.getPrice()));
        price.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnMinus = new JButton("-");
        JLabel lblQty = new JLabel(String.valueOf(item.getQuantity()));
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnPlus = new JButton("+");

        qtyPanel.add(btnMinus);
        qtyPanel.add(lblQty);
        qtyPanel.add(btnPlus);

        int subtotal = p.getPrice() * item.getQuantity();
        JLabel lblSubtotal = new JLabel("Subtotal: Rp " + String.format("%,d", subtotal));
        lblSubtotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton btnDelete = new JButton("Hapus");
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(name);
        info.add(price);
        info.add(qtyPanel);
        info.add(lblSubtotal);
        info.add(btnDelete);

        btnPlus.addActionListener(e -> {
            updateQty(item, lblQty, lblSubtotal, p.getPrice(), item.getQuantity() + 1);
        });

        btnMinus.addActionListener(e -> {
            if (item.getQuantity() > 1) {
                updateQty(item, lblQty, lblSubtotal, p.getPrice(), item.getQuantity() - 1);
            }
        });

        btnDelete.addActionListener(e -> deleteItem(item));

        card.add(img, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    private void updateQty(CartItem item, JLabel lblQty, JLabel lblSubtotal, Integer price, int newQty) {
        try {
            cartItemsController.updateQuantity(item.getCart_item_id(), newQty);
            item.setQuantity(newQty);
            lblQty.setText(String.valueOf(newQty));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal update qty: " + e.getMessage());
        }
    }

    private void deleteItem(CartItem item) {
        try {
            cartItemsController.removeItem(item.getCart_item_id());
            loadCartData(); // reload

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JlabelKeranjang = new javax.swing.JLabel();
        pnlkeranjang1 = new javax.swing.JPanel();

        JlabelKeranjang.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        JlabelKeranjang.setForeground(new java.awt.Color(74, 112, 169));
        JlabelKeranjang.setText("Keranjang Produk");

        pnlkeranjang1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlkeranjang1Layout = new javax.swing.GroupLayout(pnlkeranjang1);
        pnlkeranjang1.setLayout(pnlkeranjang1Layout);
        pnlkeranjang1Layout.setHorizontalGroup(
            pnlkeranjang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 957, Short.MAX_VALUE)
        );
        pnlkeranjang1Layout.setVerticalGroup(
            pnlkeranjang1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JlabelKeranjang)
                    .addComponent(pnlkeranjang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(JlabelKeranjang)
                .addGap(18, 18, 18)
                .addComponent(pnlkeranjang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(462, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JlabelKeranjang;
    private javax.swing.JPanel pnlkeranjang1;
    // End of variables declaration//GEN-END:variables
}
