/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.customer;

import app.controller.CategoryController;
import app.controller.ProductController;
import app.services.CategoriesDao;
import app.services.ProductsDao;
import dao.GenericDao;
import dao.GenericDaoImpl;
import models.Category;
import models.Product;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.KF;
import ui.admin.DashboardAdmin;
import util.WrapLayout;

/**
 *
 * @author Dell
 */
public class DashboardCustomer extends javax.swing.JPanel {

    /**
     * Creates new form Dashboard
     */
    private CategoryController categoryController = CategoryController.getInstance();
    private ProductController productController = ProductController.getInstance();

    public DashboardCustomer() {
        initComponents();
        setupContainers();
        loadCategories();
        loadProducts(null);
    }

    private void setupContainers() {
        JPanel catContainer = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 10));
        catContainer.setBackground(Color.WHITE);

        JScrollPane catScroll = new JScrollPane(catContainer);
        catScroll.setBorder(null);
        catScroll.getViewport().setBackground(Color.WHITE);
        catScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelcategory.putClientProperty("container", catContainer);
        jPanelcategory.setLayout(new BorderLayout());
        jPanelcategory.setBackground(Color.WHITE);
        jPanelcategory.add(catScroll, BorderLayout.CENTER);

        JPanel prodContainer = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        prodContainer.setBackground(Color.WHITE);

        JScrollPane prodScroll = new JScrollPane(prodContainer);
        prodScroll.setBorder(null);
        prodScroll.getViewport().setBackground(Color.WHITE);
        prodScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelprod.putClientProperty("container", prodContainer);
        jPanelprod.setLayout(new BorderLayout());
        jPanelprod.setBackground(Color.WHITE);
        jPanelprod.add(prodScroll, BorderLayout.CENTER);
    }

    private JPanel createCategoryBoxWithImage(Category category, Runnable onClick) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(110, 70));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        panel.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (category.getImage_path() != null) {
            ImageIcon icon = new ImageIcon(category.getImage_path());
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
        } else {
            iconLabel.setText("No Image");
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            iconLabel.setForeground(Color.GRAY);
        }

        JLabel label = new JLabel(category.getCategory_name(), SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setBorder(new EmptyBorder(0, 0, 8, 0));

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);

        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onClick.run();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(240, 240, 240));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });

        return panel;
    }

    public void loadCategories() {
        try {
            JPanel container = (JPanel) jPanelcategory.getClientProperty("container");
            container.removeAll();

            Category all = new Category();
            all.setCategory_name("All");
            all.setImage_path("categoryimages/icon_all.png");

            container.add(createCategoryBoxWithImage(all, () -> loadProducts(null)));

            List<Category> categories = categoryController.getAllCategories();
            for (Category c : categories) {

                container.add(createCategoryBoxWithImage(c, () -> loadProducts(c)));
            }

            container.revalidate();
            container.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(180, 250));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        card.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(180, 120));
        imagePanel.setBackground(new Color(240, 240, 240));
        imagePanel.setLayout(new GridBagLayout());

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        if (p.getImage_path() != null && !p.getImage_path().isEmpty()) {
            try {
                File file = new File(p.getImage_path());
                if (file.exists()) {
                    BufferedImage img = ImageIO.read(file);

                    // Resize to fit panel
                    Image scaled = img.getScaledInstance(
                            160, // width
                            110, // height
                            Image.SCALE_SMOOTH
                    );

                    imageLabel.setIcon(new ImageIcon(scaled));
                } else {
                    imageLabel.setText("Image Not Found");
                }
            } catch (Exception e) {
                imageLabel.setText("Error Loading Image");
            }
        } else {
            imageLabel.setText("No Image");
        }

        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        imageLabel.setForeground(new Color(100, 100, 100));

        imagePanel.add(imageLabel);
        card.add(imagePanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel name = new JLabel(p.getProduct_name());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel price = new JLabel("Rp " + p.getPrice());
        price.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridy = 0;
        infoPanel.add(name, gbc);
        gbc.gridy = 1;
        infoPanel.add(price, gbc);

        card.add(infoPanel, BorderLayout.CENTER);

        JButton btn = new JButton("View");
        btn.setFocusPainted(false);
        btn.setBackground(new Color(74,112,169));
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            JPanel pnlUtama = (JPanel) SwingUtilities.getAncestorOfClass(JPanel.class, this);
            KF.UntukPanel(pnlUtama, KF.fDetailProductCustomer);
            KF.fDetailProductCustomer.loadData(p);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(Color.WHITE);
        bottom.add(btn);

        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    public void loadProducts(Category category) {
        try {
            JPanel container = (JPanel) jPanelprod.getClientProperty("container");
            container.removeAll();

            List<Product> products;

            if (category == null) {
                products = productController.getAllProducts();

            } else {
                int mainId = category.getCategory_id();

                List<Integer> childIds = categoryController.getAllChildIds(mainId);
                childIds.add(mainId);

                products = productController.getProductsByCategoryList(childIds);
            }

            for (Product p : products) {
                if (Boolean.TRUE.equals(p.getIs_active())) {
                    container.add(createProductCard(p));
                }
            }

            container.revalidate();
            container.repaint();

        } catch (Exception e) {
            e.printStackTrace();
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

        jLabelCategory = new javax.swing.JLabel();
        jLabelProducts = new javax.swing.JLabel();
        jPanelcategory = new javax.swing.JPanel();
        jPanelprod = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabelCategory.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelCategory.setForeground(new java.awt.Color(74, 112, 169));
        jLabelCategory.setText("Categories");

        jLabelProducts.setBackground(new java.awt.Color(74, 112, 169));
        jLabelProducts.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelProducts.setForeground(new java.awt.Color(74, 112, 169));
        jLabelProducts.setText("Products");

        jPanelcategory.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanelcategoryLayout = new javax.swing.GroupLayout(jPanelcategory);
        jPanelcategory.setLayout(jPanelcategoryLayout);
        jPanelcategoryLayout.setHorizontalGroup(
            jPanelcategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1016, Short.MAX_VALUE)
        );
        jPanelcategoryLayout.setVerticalGroup(
            jPanelcategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 137, Short.MAX_VALUE)
        );

        jPanelprod.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelcategory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelprod, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelCategory, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelProducts, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(100, 100, 100))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabelCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelcategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabelProducts)
                .addGap(18, 18, 18)
                .addComponent(jPanelprod, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Add1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelCategory;
    private javax.swing.JLabel jLabelProducts;
    private javax.swing.JPanel jPanelcategory;
    private javax.swing.JPanel jPanelprod;
    // End of variables declaration//GEN-END:variables
}
