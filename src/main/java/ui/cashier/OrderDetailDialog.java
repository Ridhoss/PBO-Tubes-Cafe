package ui.cashier;

import app.controller.CashierController;
import models.Order;
import models.OrderItem;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OrderDetailDialog extends JDialog {

    private CashierController controller;
    private Order order;
    private boolean orderUpdated = false;

    private JLabel lblStatusValue;
    private JButton btnApprove;
    private JButton btnPay;

    public OrderDetailDialog(JFrame parent, CashierController controller, Order order) {
        super(parent, "Detail Pesanan: " + order.getOrderId(), true);
        this.controller = controller;
        this.order = order;

        setSize(650, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        // --- 1. HEADER INFO ---
        JPanel pnlInfo = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlInfo.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlInfo.setBackground(Color.WHITE);

        addInfo(pnlInfo, "Order ID:", String.valueOf(order.getOrderId()));
        addInfo(pnlInfo, "Nama Customer:", order.getCustomerName());
        addInfo(pnlInfo, "Nomor Meja:", order.getTableNumber());
        addInfo(pnlInfo, "Metode Bayar:", order.getPaymentTypeForUI());

        JLabel lblStatusLabel = new JLabel("Status Bayar:");
        lblStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblStatusValue = new JLabel(order.isPaid() ? "LUNAS" : "BELUM LUNAS");
        lblStatusValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatusValue.setForeground(order.isPaid() ? CafeColors.STATUS_PAID : CafeColors.STATUS_UNPAID);

        pnlInfo.add(lblStatusLabel);
        pnlInfo.add(lblStatusValue);

        add(pnlInfo, BorderLayout.NORTH);

        // --- 2. TABEL ITEM ---
        String[] columns = { "Nama Menu", "Qty", "Notes", "Subtotal" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (OrderItem item : order.getItems()) {
            model.addRow(new Object[] {
                    item.getProduct().getProduct_name(),
                    item.getQuantity(),
                    item.getNotes(),
                    "Rp " + (int) item.getSubtotal()
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

        // --- 3. FOOTER ---
        JPanel pnlFooter = new JPanel(new GridLayout(2, 1, 0, 15));
        pnlFooter.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlFooter.setBackground(CafeColors.BACKGROUND);

        JPanel pnlTotalRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTotalRow.setOpaque(false);

        JLabel lblTotal = new JLabel("Total: Rp " + (int) order.getTotalAmount());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(CafeColors.PRIMARY);
        pnlTotalRow.add(lblTotal);

        JPanel pnlButtonsRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtonsRow.setOpaque(false);

        JButton btnClose = new JButton("Tutup");
        styleBaseButton(btnClose); // Hanya style dasar
        setButtonActive(btnClose, "Tutup", Color.GRAY); // Set warna
        btnClose.addActionListener(e -> dispose());

        btnPay = new JButton("Terima Pembayaran");
        styleBaseButton(btnPay);
        setButtonActive(btnPay, "Terima Pembayaran", CafeColors.PRIMARY);
        btnPay.addActionListener(e -> processPayment());

        btnApprove = new JButton("Approve Pesanan");
        styleBaseButton(btnApprove);
        // State awal diatur di updateButtonState

        pnlButtonsRow.add(btnClose);
        pnlButtonsRow.add(btnPay);
        pnlButtonsRow.add(btnApprove);

        pnlFooter.add(pnlTotalRow);
        pnlFooter.add(pnlButtonsRow);

        add(pnlFooter, BorderLayout.SOUTH);

        // Set State Awal
        updateButtonState();
    }

    private void updateButtonState() {
        if (!order.isPaid()) {
            // CASE: BELUM LUNAS
            btnPay.setVisible(true);

            // FIX BUG 1: Matikan efek hover dan set abu-abu permanen
            setButtonDisabled(btnApprove, "Menunggu Pembayaran");

        } else {
            // CASE: SUDAH LUNAS
            btnPay.setVisible(false);

            String status = order.getOrderStatus();

            if ("PENDING".equalsIgnoreCase(status)) {
                // Siap di-Approve (HIJAU)
                setButtonActive(btnApprove, "Approve Pesanan", CafeColors.STATUS_PAID);

                // Refresh listeners
                for (var al : btnApprove.getActionListeners())
                    btnApprove.removeActionListener(al);
                btnApprove.addActionListener(e -> processApproval());

            } else if ("PROCESSING".equalsIgnoreCase(status)) {
                // Sedang diproses -> Siap diselesaikan
                // FIX REQUEST 2: Warna tombol "Selesaikan" jadi HIJAU (STATUS_PAID)
                setButtonActive(btnApprove, "Selesaikan Pesanan", CafeColors.STATUS_PAID);

                for (var al : btnApprove.getActionListeners())
                    btnApprove.removeActionListener(al);
                btnApprove.addActionListener(e -> processCompletion());

            } else {
                // Completed
                setButtonDisabled(btnApprove, "Pesanan Selesai");
            }
        }
        btnPay.revalidate();
        btnApprove.revalidate();
    }

    // --- HELPER BARU UNTUK MENGATUR WARNA & HOVER ---

    /**
     * Menerapkan style dasar (Font, Border, UI) tanpa Warna/Listener
     */
    private void styleBaseButton(JButton btn) {
        btn.setUI(new BasicButtonUI()); // Hilangkan style Windows
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Mengaktifkan tombol: Set Warna + Tambah Efek Hover
     */
    private void setButtonActive(JButton btn, String text, Color color) {
        btn.setText(text);
        btn.setEnabled(true);
        btn.setBackground(color);

        // Hapus listener lama agar tidak menumpuk
        removeHoverListeners(btn);

        // Tambah listener hover baru sesuai warna
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled())
                    btn.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled())
                    btn.setBackground(color);
            }
        });
    }

    /**
     * Mematikan tombol: Warna Abu-abu + Hapus Efek Hover
     */
    private void setButtonDisabled(JButton btn, String text) {
        btn.setText(text);
        btn.setEnabled(false);
        btn.setBackground(Color.LIGHT_GRAY);

        // Hapus semua efek hover agar tidak berubah warna saat mouse lewat
        removeHoverListeners(btn);
    }

    private void removeHoverListeners(JButton btn) {
        for (MouseListener ml : btn.getMouseListeners()) {
            // Hapus listener yang kita buat (Instance of MouseAdapter)
            // Note: BasicButtonUI mungkin punya listener sendiri, tapi ini aman
            if (ml instanceof MouseAdapter) {
                btn.removeMouseListener(ml);
            }
        }
    }

    // --- LOGIC PROSES ---

    private void processPayment() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Terima pembayaran senilai Rp " + (int) order.getTotalAmount() + "?",
                "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.markOrderAsPaid(order.getOrderId(), order.getTotalAmount());
            if (success) {
                orderUpdated = true;
                order.setPaymentStatus("paid");
                lblStatusValue.setText("LUNAS");
                lblStatusValue.setForeground(CafeColors.STATUS_PAID);
                updateButtonState();
            }
        }
    }

    private void processApproval() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Approve pesanan ini dan kirim ke dapur?",
                "Konfirmasi Approve", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.approveOrder(order.getOrderId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Pesanan Masuk Antrian Dapur.");
                orderUpdated = true;
                dispose();
            }
        }
    }

    private void processCompletion() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tandai pesanan ini sebagai SELESAI (Sudah disajikan)?",
                "Konfirmasi Selesai", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.completeOrder(order.getOrderId());
            if (success) {
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

    public boolean isOrderUpdated() {
        return orderUpdated;
    }
}