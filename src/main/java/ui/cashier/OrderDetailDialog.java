package ui.cashier;

import app.controller.CashierController;
import models.Order;
import models.OrderItem;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrderDetailDialog extends JDialog {

    private CashierController controller;
    private Order order;
    private boolean orderUpdated = false; 

    // Komponen UI yang perlu diakses global di class ini
    private JLabel lblStatusValue;
    private JButton btnApprove;
    private JButton btnPay; // Tombol baru untuk bayar

    public OrderDetailDialog(JFrame parent, CashierController controller, Order order) {
        super(parent, "Detail Pesanan: " + order.getOrderId(), true); 
        this.controller = controller;
        this.order = order;
        
        setSize(650, 550); // Sedikit diperbesar
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        // --- 1. HEADER INFO ---
        JPanel pnlInfo = new JPanel(new GridLayout(5, 2, 5, 5)); // Update row jadi 5
        pnlInfo.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlInfo.setBackground(Color.WHITE);

        addInfo(pnlInfo, "Order ID:", order.getOrderId()); // Tambah info ID buat verifikasi
        addInfo(pnlInfo, "Nama Customer:", order.getCustomerName());
        addInfo(pnlInfo, "Nomor Meja:", order.getTableNumber());
        addInfo(pnlInfo, "Metode Bayar:", order.getPaymentType());
        
        JLabel lblStatusLabel = new JLabel("Status Bayar:");
        lblStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        lblStatusValue = new JLabel(order.isPaid() ? "LUNAS" : "BELUM LUNAS");
        lblStatusValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatusValue.setForeground(order.isPaid() ? CafeColors.STATUS_PAID : CafeColors.STATUS_UNPAID);
        
        pnlInfo.add(lblStatusLabel);
        pnlInfo.add(lblStatusValue);

        add(pnlInfo, BorderLayout.NORTH);

        // --- 2. TABEL ITEM ---
        String[] columns = {"Nama Menu", "Qty", "Notes", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (OrderItem item : order.getItems()) {
            model.addRow(new Object[]{
                item.getProduct().getProduct_name(),
                item.getQuantity(),
                item.getNotes(),
                "Rp " + (int)item.getSubtotal()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. FOOTER (TOTAL & ACTIONS) ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlFooter.setBackground(CafeColors.BACKGROUND);

        JLabel lblTotal = new JLabel("Total: Rp " + (int)order.getTotalAmount());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(CafeColors.PRIMARY);
        pnlFooter.add(lblTotal, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        JButton btnClose = new JButton("Tutup");
        styleButton(btnClose, Color.GRAY);
        btnClose.addActionListener(e -> dispose());

        // Tombol Terima Pembayaran (Khusus Cash Belum Lunas)
        btnPay = new JButton("Terima Pembayaran");
        styleButton(btnPay, CafeColors.PRIMARY);
        btnPay.addActionListener(e -> processPayment());

        // Tombol Approve
        btnApprove = new JButton("Approve Pesanan");
        styleButton(btnApprove, CafeColors.STATUS_PAID);
        btnApprove.addActionListener(e -> processApproval());

        pnlButtons.add(btnClose);
        pnlButtons.add(btnPay);     // Tambahkan tombol Bayar
        pnlButtons.add(btnApprove); // Tambahkan tombol Approve
        pnlFooter.add(pnlButtons, BorderLayout.EAST);

        add(pnlFooter, BorderLayout.SOUTH);

        // Update State Tombol Awal
        updateButtonState();
    }

    private void updateButtonState() {
        // Jika belum lunas, tombol Approve mati, tombol Bayar nyala
        if (!order.isPaid()) {
            btnPay.setVisible(true);
            btnApprove.setEnabled(false);
            btnApprove.setText("Menunggu Pembayaran");
            btnApprove.setBackground(Color.LIGHT_GRAY);
        } else {
            // Jika sudah lunas
            btnPay.setVisible(false); // Sembunyikan tombol bayar
            
            if (order.getOrderStatus().equals("PENDING")) {
                btnApprove.setEnabled(true);
                btnApprove.setText("Approve Pesanan");
                btnApprove.setBackground(CafeColors.STATUS_PAID);
            } else {
                btnApprove.setEnabled(false);
                btnApprove.setText("Sudah Diproses");
                btnApprove.setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private void processPayment() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Terima pembayaran senilai Rp " + (int)order.getTotalAmount() + "?",
            "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.markOrderAsPaid(order.getOrderId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil Diterima!");
                orderUpdated = true;
                
                // Update UI langsung tanpa tutup dialog
                lblStatusValue.setText("LUNAS");
                lblStatusValue.setForeground(CafeColors.STATUS_PAID);
                updateButtonState();
            }
        }
    }

    private void processApproval() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Approve pesanan ini dan kirim ke dapur?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.approveOrder(order.getOrderId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Pesanan Berhasil Di-Approve!");
                orderUpdated = true;
                dispose();
            }
        }
    }

    private void addInfo(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(val);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public boolean isOrderUpdated() {
        return orderUpdated;
    }
}