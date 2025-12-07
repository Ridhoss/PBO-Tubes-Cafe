package ui.cashier;

import app.controller.CashierController;
import app.controller.OrderController;
import models.Order;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BarcodeScannerDialog extends JDialog {

    CashierController cashiercontroller = CashierController.getInstance();
    OrderController ordercontroller = OrderController.getInstance();
    
    private JTextField txtBarcode;
    private boolean scanSuccess = false; // Flag untuk refresh dashboard

    public BarcodeScannerDialog(JFrame parent, CashierController controller) {
        super(parent, "Scan Barcode / QR", true); // Modal true

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        initComponents(parent);
    }

    private void initComponents(JFrame parent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(CafeColors.BACKGROUND);

        // Icon
        JLabel lblIcon = new JLabel("📷"); 
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label Instruksi
        JLabel lblInstruction = new JLabel("Scan Barcode / Masukkan Kode Order:");
        lblInstruction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Field
        txtBarcode = new JTextField();
        txtBarcode.setFont(new Font("Consolas", Font.BOLD, 20));
        txtBarcode.setHorizontalAlignment(JTextField.CENTER);
        txtBarcode.setMaximumSize(new Dimension(300, 50));
        
        // Tombol Cari
        JButton btnSearch = new JButton("Cari Pesanan");
        btnSearch.setBackground(CafeColors.PRIMARY);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // --- LOGIC SEARCH ---
        Runnable doScan = () -> {
            try {
                String code = txtBarcode.getText().trim();
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Kode tidak boleh kosong!");
                    return;
                }
                
                // Cari order via Controller (Bisa by ID integer atau String Code)
                // Karena getOrderDetail terima String, pastikan controller handle parsing
                Order order = cashiercontroller.getOrderDetail(code);
                
                if (order != null) {
                    // Tutup scanner dulu agar terlihat seamless
                    dispose(); 
                    
                    // Buka Detail Pesanan
                    SwingUtilities.invokeLater(() -> {
                        OrderDetailDialog detailDialog = new OrderDetailDialog(parent, cashiercontroller, ordercontroller, order);
                        detailDialog.setVisible(true);
                        
                        // Cek apakah ada update status di dialog
                        if (detailDialog.isOrderUpdated()) {
                            scanSuccess = true; 
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Order dengan ID/Kode '" + code + "' tidak ditemukan!", "Not Found", JOptionPane.ERROR_MESSAGE);
                    txtBarcode.selectAll();
                    txtBarcode.requestFocus();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Database: " + ex.getMessage());
            }
        };

        // Listeners
        btnSearch.addActionListener(e -> doScan.run());
        
        txtBarcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doScan.run();
                }
            }
        });

        panel.add(lblIcon);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblInstruction);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtBarcode);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnSearch);

        add(panel, BorderLayout.CENTER);
    }
    
    // Getter untuk Dashboard tahu harus refresh atau tidak
    public boolean isScanSuccess() {
        return scanSuccess;
    }
}