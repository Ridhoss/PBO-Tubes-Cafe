package ui.cashier;

import app.controller.CashierController;
import models.Order;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CashierDashboard extends JPanel {

    private CashierController controller;

    // Komponen Tabel
    private JTable tblUnpaid;   // Tabel Kiri (Cash/Belum Lunas)
    private JTable tblPaid;     // Tabel Kanan (Debit/Lunas)
    private JTable tblProcessed;// Tabel Tab 2 (Monitoring)

    // Model Data Tabel
    private DefaultTableModel modelUnpaid;
    private DefaultTableModel modelPaid;
    private DefaultTableModel modelProcessed;

    public CashierDashboard() {
        this.controller = new CashierController();
        initComponents();
        refreshData(); // Load data awal saat panel dibuka
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(CafeColors.BACKGROUND);

        // --- 1. HEADER SECTION ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(CafeColors.PRIMARY);
        pnlHeader.setPreferredSize(new Dimension(800, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Cashier Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CafeColors.WHITE);

        // Panel Tombol di Header
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setOpaque(false);

        JButton btnScan = createButton("Scan Barcode", "/images/icon_card.png"); // Ganti icon jika ada
        JButton btnRefresh = createButton("Refresh Data", null);

        // Event Listener Tombol Refresh
        btnRefresh.addActionListener((ActionEvent e) -> {
            refreshData();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        // Event Listener Tombol Scan (Akan diisi di Tahap 5)
        btnScan.addActionListener(e -> {
             JOptionPane.showMessageDialog(this, "Fitur Scan akan dibuat di Tahap 5");
             // new BarcodeScannerDialog((JFrame) SwingUtilities.getWindowAncestor(this), controller).setVisible(true);
             refreshData();
        });

        pnlActions.add(btnScan);
        pnlActions.add(btnRefresh);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlActions, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. MAIN CONTENT (TABS) ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Tab 1: Pesanan Masuk (Split View)
        JPanel pnlIncoming = new JPanel(new GridLayout(1, 2, 10, 0)); // Grid 1 baris 2 kolom
        pnlIncoming.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlIncoming.setBackground(CafeColors.BACKGROUND);

        // Panel Kiri: Belum Lunas (Cash)
        modelUnpaid = createTableModel();
        tblUnpaid = createTable(modelUnpaid);
        addTableListener(tblUnpaid);
        JPanel pnlLeft = createTablePanel("Belum Lunas (Cash)", tblUnpaid, CafeColors.STATUS_UNPAID);

        // Panel Kanan: Sudah Lunas (Debit)
        modelPaid = createTableModel();
        tblPaid = createTable(modelPaid);
        addTableListener(tblPaid);
        JPanel pnlRight = createTablePanel("Sudah Lunas (Debit/QRIS)", tblPaid, CafeColors.STATUS_PAID);

        pnlIncoming.add(pnlLeft);
        pnlIncoming.add(pnlRight);

        // Tab 2: Monitoring (Processed)
        modelProcessed = createTableModel();
        tblProcessed = createTable(modelProcessed);
        addTableListener(tblProcessed);
        JPanel pnlMonitoring = createTablePanel("Riwayat & Proses Pesanan", tblProcessed, CafeColors.SECONDARY);
        pnlMonitoring.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane.addTab("Pesanan Masuk", pnlIncoming);
        tabbedPane.addTab("Monitoring Pesanan", pnlMonitoring);

        add(tabbedPane, BorderLayout.CENTER);
    }

    // --- HELPER METHODS UNTUK MEMBUAT UI ---

    private JButton createButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(CafeColors.SECONDARY);
        btn.setForeground(CafeColors.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID Order", "Nama", "Meja", "Total", "Waktu", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit langsung
            }
        };
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(CafeColors.TEXT_DARK);
        
        // Center Alignment untuk beberapa kolom
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Meja
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Status

        return table;
    }

    private JPanel createTablePanel(String title, JTable table, Color headerColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Header Panel Kecil di atas tabel
        JLabel lblHeader = new JLabel(title);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(headerColor);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Hint Text di bawah tabel
        JLabel lblHint = new JLabel(" *Klik baris untuk melihat detail & aksi");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(lblHint, BorderLayout.SOUTH);

        return panel;
    }

    // --- LOGIC UPDATE DATA ---

    public void refreshData() {
        // 1. Clear Data Lama
        modelUnpaid.setRowCount(0);
        modelPaid.setRowCount(0);
        modelProcessed.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // 2. Isi Tabel Unpaid (Cash)
        List<Order> unpaidOrders = controller.getUnpaidPendingOrders();
        for (Order o : unpaidOrders) {
            modelUnpaid.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomerName(),
                o.getTableNumber(),
                "Rp " + (int)o.getTotalAmount(),
                o.getOrderTime().format(formatter),
                "BELUM LUNAS"
            });
        }

        // 3. Isi Tabel Paid (Debit)
        List<Order> paidOrders = controller.getPaidPendingOrders();
        for (Order o : paidOrders) {
            modelPaid.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomerName(),
                o.getTableNumber(),
                "Rp " + (int)o.getTotalAmount(),
                o.getOrderTime().format(formatter),
                "LUNAS (Verifikasi)"
            });
        }

        // 4. Isi Tabel Processed
        List<Order> processedOrders = controller.getProcessedOrders();
        for (Order o : processedOrders) {
            modelProcessed.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomerName(),
                o.getTableNumber(),
                "Rp " + (int)o.getTotalAmount(),
                o.getOrderTime().format(formatter),
                o.getOrderStatus()
            });
        }
    }

    // Method baru untuk mendeteksi klik pada tabel
    private void addTableListener(JTable table) {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row == -1) return;

                // Ambil Order ID dari kolom ke-0
                String orderId = (String) table.getValueAt(row, 0);
                
                // Ambil detail order dari controller
                Order order = controller.getOrderDetail(orderId);
                
                if (order != null) {
                    // Buka Dialog Detail
                    // Menggunakan SwingUtilities untuk mendapatkan Frame parent
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CashierDashboard.this);
                    OrderDetailDialog dialog = new OrderDetailDialog(parentFrame, controller, order);
                    dialog.setVisible(true); // Aplikasi akan berhenti di sini sampai dialog ditutup (Modal)

                    // Setelah dialog ditutup, cek apakah perlu refresh
                    if (dialog.isOrderUpdated()) {
                        refreshData();
                    }
                }
            }
        });
    }

    // Main method untuk testing tampilan ini secara mandiri (Tanpa Login)
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Cashier System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.add(new CashierDashboard());
        frame.setVisible(true);
    }
}