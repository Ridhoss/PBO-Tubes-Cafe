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
    private boolean orderUpdated = false; // Flag untuk memberitahu dashboard ada perubahan

    public OrderDetailDialog(JFrame parent, CashierController controller, Order order) {
        super(parent, "Detail Pesanan: " + order.getOrderId(), true); // Modal = true
        this.controller = controller;
        this.order = order;
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        // --- 1. HEADER INFO ---
        JPanel pnlInfo = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlInfo.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlInfo.setBackground(Color.WHITE);

        addInfo(pnlInfo, "Nama Customer:", order.getCustomerName());
        addInfo(pnlInfo, "Nomor Meja:", order.getTableNumber());
        addInfo(pnlInfo, "Metode Bayar:", order.getPaymentType());
        
        // Status Pembayaran dengan Warna
        JLabel lblStatusLabel = new JLabel("Status Bayar:");
        lblStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblStatusValue = new JLabel(order.isPaid() ? "LUNAS" : "BELUM LUNAS");
        lblStatusValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Rata Tengah untuk Qty
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. FOOTER (TOTAL & ACTIONS) ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlFooter.setBackground(CafeColors.BACKGROUND);

        // Total Label
        JLabel lblTotal = new JLabel("Total: Rp " + (int)order.getTotalAmount());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(CafeColors.PRIMARY);
        pnlFooter.add(lblTotal, BorderLayout.WEST);

        // Tombol Aksi
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setOpaque(false);

        JButton btnClose = new JButton("Tutup");
        styleButton(btnClose, Color.GRAY);
        btnClose.addActionListener(e -> dispose());

        JButton btnApprove = new JButton("Approve Pesanan");
        styleButton(btnApprove, CafeColors.STATUS_PAID);

        // LOGIKA PENTING: Tombol Approve
        // Hanya bisa diklik jika sudah LUNAS dan Status masih PENDING
        if (order.isPaid() && order.getOrderStatus().equals("PENDING")) {
            btnApprove.setEnabled(true);
            btnApprove.setToolTipText("Siap diproses ke dapur");
        } else if (!order.isPaid()) {
            btnApprove.setEnabled(false);
            btnApprove.setText("Menunggu Pembayaran");
            btnApprove.setBackground(Color.LIGHT_GRAY);
        } else {
            btnApprove.setEnabled(false); // Sudah diproses
            btnApprove.setText("Sudah Diproses");
        }

        // Action Approve
        btnApprove.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Approve pesanan ini dan kirim ke dapur?", 
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = controller.approveOrder(order.getOrderId());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Pesanan Berhasil Di-Approve!");
                    orderUpdated = true; // Tandai ada perubahan agar dashboard refresh
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal! Pastikan pesanan sudah lunas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pnlButtons.add(btnClose);
        pnlButtons.add(btnApprove);
        pnlFooter.add(pnlButtons, BorderLayout.EAST);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    // --- Helper Methods ---
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