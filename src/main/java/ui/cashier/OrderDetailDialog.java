package ui.cashier;

import app.controller.CashierController;
import app.controller.OrderController;
import app.controller.OrderItemsController;
import app.controller.ProductController; // Integrasi Product
import app.controller.UserController;    // Integrasi User
import models.Order;
import models.OrderItems;
import models.Product;
import models.User;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class OrderDetailDialog extends JDialog {

    private CashierController controller;
    private Order order;
    private boolean orderUpdated = false;

    private JLabel lblStatusValue;
    private JButton btnApprove, btnPay;

    OrderItemsController orderitemcontroller = OrderItemsController.getInstance();
    ProductController productController = ProductController.getInstance();
    UserController userController = UserController.getInstance();

    public OrderDetailDialog(JFrame parent, CashierController controller, OrderController ordercon, Order order) {
        super(parent, "Detail Pesanan: " + order.getOrderCode(), true);
        this.controller = controller;
        this.order = order;

        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        try {
            JPanel pnlInfo = new JPanel(new GridLayout(5, 2, 5, 5));
            pnlInfo.setBorder(new EmptyBorder(20, 20, 20, 20));
            pnlInfo.setBackground(Color.WHITE);

            // Integrasi: Ambil Nama Customer
            String custName = "-";
            try {
                User u = userController.findById(order.getUser_id());
                if (u != null) custName = u.getFull_name();
            } catch (Exception e) {}

            addInfo(pnlInfo, "Kode Order:", order.getOrderCode());
            addInfo(pnlInfo, "Customer:", custName);
            addInfo(pnlInfo, "Meja:", order.getTableCode());
            addInfo(pnlInfo, "Pembayaran:", order.getPayment_status());

            JLabel lblStatus = new JLabel("Status Pesanan:");
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblStatusValue = new JLabel(order.getOrder_status());
            lblStatusValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
            
            // Set Warna Status
            String status = order.getOrder_status();
            if ("completed".equalsIgnoreCase(status)) lblStatusValue.setForeground(CafeColors.STATUS_PAID);
            else if ("processing".equalsIgnoreCase(status)) lblStatusValue.setForeground(CafeColors.STATUS_PROCESS);
            else lblStatusValue.setForeground(CafeColors.STATUS_UNPAID);

            pnlInfo.add(lblStatus);
            pnlInfo.add(lblStatusValue);
            add(pnlInfo, BorderLayout.NORTH);

            // --- TABEL ITEM ---
            String[] cols = {"Menu", "Qty", "Harga", "Subtotal", "Catatan"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);
            
            List<OrderItems> items = orderitemcontroller.getItemsByOrderId(order.getOrder_id());
            for (OrderItems item : items) {
                // Integrasi: Ambil Nama Produk
                String prodName = "Unknown ID:" + item.getProduct_id();
                int price = 0;
                try {
                    Product p = productController.getProductById(item.getProduct_id());
                    if (p != null) {
                        prodName = p.getProduct_name();
                        price = p.getPrice();
                    }
                } catch (Exception e) {}

                model.addRow(new Object[]{
                    prodName, 
                    item.getQuantity(), 
                    "Rp " + price, 
                    "Rp " + item.getSubtotal(), 
                    item.getNote()
                });
            }

            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
            scroll.getViewport().setBackground(Color.WHITE);
            add(scroll, BorderLayout.CENTER);

            // --- FOOTER ---
            JPanel pnlFooter = new JPanel(new BorderLayout());
            pnlFooter.setBorder(new EmptyBorder(15,20,15,20));
            pnlFooter.setBackground(CafeColors.BACKGROUND);

            // Gunakan Final Amount agar tidak Rp Null
            JLabel lblTotal = new JLabel("Total: Rp " + (int) order.getFinal_amount());
            lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblTotal.setForeground(CafeColors.PRIMARY);

            JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pnlBtn.setOpaque(false);

            JButton btnClose = createBtn("Tutup", Color.GRAY);
            btnClose.addActionListener(e -> dispose());

            btnPay = createBtn("Terima Bayar", CafeColors.STATUS_UNPAID);
            btnPay.addActionListener(e -> processPayment());

            btnApprove = createBtn("Proses", CafeColors.STATUS_PAID);
            // Action listener diset dinamis

            pnlBtn.add(btnClose);
            pnlBtn.add(btnPay);
            pnlBtn.add(btnApprove);

            pnlFooter.add(lblTotal, BorderLayout.WEST);
            pnlFooter.add(pnlBtn, BorderLayout.EAST);
            add(pnlFooter, BorderLayout.SOUTH);

            updateButtonState();

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void updateButtonState() {
        String payStatus = order.getPayment_status();
        String ordStatus = order.getOrder_status();

        for (var al : btnApprove.getActionListeners()) btnApprove.removeActionListener(al);

        if (!"paid".equalsIgnoreCase(payStatus)) {
            // Belum bayar
            btnPay.setVisible(true);
            btnApprove.setEnabled(false);
            btnApprove.setBackground(Color.LIGHT_GRAY);
            btnApprove.setText("Menunggu Bayar");
        } else {
            // Sudah bayar
            btnPay.setVisible(false);
            btnApprove.setEnabled(true);

            if ("pending".equalsIgnoreCase(ordStatus) || "Waiting".equalsIgnoreCase(ordStatus)) {
                btnApprove.setText("Approve & Masak");
                btnApprove.setBackground(CafeColors.STATUS_PAID);
                btnApprove.addActionListener(e -> processAction("approve"));
            } else if ("processing".equalsIgnoreCase(ordStatus)) {
                btnApprove.setText("Selesaikan Order");
                btnApprove.setBackground(CafeColors.STATUS_PROCESS);
                btnApprove.addActionListener(e -> processAction("complete"));
            } else {
                btnApprove.setEnabled(false);
                btnApprove.setBackground(Color.GRAY);
                btnApprove.setText("Selesai");
            }
        }
    }

    private void processPayment() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Terima pembayaran Rp " + (int) order.getFinal_amount() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.markOrderAsPaid(order.getOrder_id(), order.getFinal_amount());
                orderUpdated = true;
                order.setPayment_status("paid");
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil!");
                updateButtonState();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }
    }

    private void processAction(String action) {
        try {
            if ("approve".equals(action)) {
                controller.approveOrder(order.getOrder_id());
                order.setOrder_status("processing");
                lblStatusValue.setText("processing");
                JOptionPane.showMessageDialog(this, "Order dikirim ke dapur.");
            } else {
                controller.completeOrder(order.getOrder_id());
                order.setOrder_status("completed");
                JOptionPane.showMessageDialog(this, "Order Selesai.");
                dispose();
            }
            orderUpdated = true;
            updateButtonState();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setUI(new BasicButtonUI());
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8,15,8,15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addInfo(JPanel p, String l, String v) {
        p.add(new JLabel(l));
        JLabel val = new JLabel(v);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.add(val);
    }

    public boolean isOrderUpdated() { return orderUpdated; }
}