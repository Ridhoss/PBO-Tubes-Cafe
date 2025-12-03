package ui.cashier;

import app.controller.CashierController;
import models.Order;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BarcodeScannerDialog extends JDialog {

    private CashierController controller;
    private JTextField txtBarcode;
    private boolean scanSuccess = false;

    public BarcodeScannerDialog(JFrame parent, CashierController controller) {
        super(parent, "Scan Barcode / QR", true);
        this.controller = controller;

        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        initComponents(parent);
    }

    private void initComponents(JFrame parent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(CafeColors.BACKGROUND);

        // Icon & Label
        JLabel lblIcon = new JLabel("📷"); // Placeholder Icon
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInstruction = new JLabel("Scan Barcode Customer di sini:");
        lblInstruction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Field (Simulasi Scanner)
        txtBarcode = new JTextField();
        txtBarcode.setFont(new Font("Consolas", Font.BOLD, 18));
        txtBarcode.setHorizontalAlignment(JTextField.CENTER);
        txtBarcode.setMaximumSize(new Dimension(300, 40));
        
        // Tombol Aksi
        JButton btnSimulate = new JButton("Simulasi Scan");
        btnSimulate.setBackground(CafeColors.PRIMARY);
        btnSimulate.setForeground(Color.WHITE);
        btnSimulate.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Logic Scan
        Runnable doScan = () -> {
            String code = txtBarcode.getText().trim();
            if (code.isEmpty()) return;

            Order order = controller.getOrderDetail(code);
            if (order != null) {
                scanSuccess = true;
                dispose(); // Tutup scanner
                
                // Buka Detail Pesanan Langsung
                SwingUtilities.invokeLater(() -> {
                    OrderDetailDialog detailDialog = new OrderDetailDialog(parent, controller, order);
                    detailDialog.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this, "Order ID tidak ditemukan!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                txtBarcode.selectAll();
            }
        };

        btnSimulate.addActionListener(e -> doScan.run());
        
        // Enter key listener (Scanner fisik biasanya kirim ENTER setelah scan)
        txtBarcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doScan.run();
                }
            }
        });

        panel.add(lblIcon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblInstruction);
        panel.add(Box.createVerticalStrut(15));
        panel.add(txtBarcode);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnSimulate);

        add(panel, BorderLayout.CENTER);
    }
    
    public boolean isScanSuccess() {
        return scanSuccess;
    }
}