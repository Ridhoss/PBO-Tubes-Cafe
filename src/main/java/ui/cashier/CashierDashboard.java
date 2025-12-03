package ui.cashier;

import app.controller.CashierController;
import models.Order;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI; // Wajib untuk warna tombol
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CashierDashboard extends JPanel {

    private CashierController controller;

    // --- KOMPONEN TABEL ---
    private JTable tblUnpaid; // Atas (Cash/Belum)
    private JTable tblPaid; // Bawah (Debit/Lunas)
    private JTable tblProcessing; // Tab 2 (Dapur)
    private JTable tblCompleted; // Tab 3 (Selesai)

    private DefaultTableModel modelUnpaid, modelPaid, modelProcessing, modelCompleted;

    public CashierDashboard() {
        this.controller = new CashierController();
        initComponents();
        refreshData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(CafeColors.BACKGROUND);

        // =========================================
        // 1. HEADER (Top Bar)
        // =========================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 25, 15, 25)));

        JLabel lblTitle = new JLabel("Cashier Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CafeColors.PRIMARY);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setOpaque(false);

        JButton btnScan = createStyledButton("Scan Barcode", CafeColors.PRIMARY);
        JButton btnRefresh = createStyledButton("Refresh Data", CafeColors.SECONDARY);

        // --- REVISI 1: Tambahkan Notifikasi Refresh ---
        btnRefresh.addActionListener((ActionEvent e) -> {
            refreshData();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        btnScan.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            BarcodeScannerDialog scanner = new BarcodeScannerDialog(parentFrame, controller);
            scanner.setVisible(true);

            if (scanner.isScanSuccess()) {
                refreshData();
            }
        });

        pnlActions.add(btnScan);
        pnlActions.add(btnRefresh);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlActions, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // =========================================
        // 2. MAIN TABS
        // =========================================
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(CafeColors.BACKGROUND);

        // --- TAB 1: PESANAN MASUK (Split Atas-Bawah) ---
        JPanel pnlIncoming = new JPanel(new GridLayout(2, 1, 0, 20));
        pnlIncoming.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlIncoming.setBackground(CafeColors.BACKGROUND);

        // Tabel Atas: Belum Lunas
        modelUnpaid = createTableModel();
        tblUnpaid = createStyledTable(modelUnpaid);
        addTableListener(tblUnpaid);
        JPanel pnlUnpaid = createSectionPanel("Menunggu Pembayaran (Cash)", tblUnpaid, CafeColors.STATUS_UNPAID);

        // Tabel Bawah: Sudah Lunas
        modelPaid = createTableModel();
        tblPaid = createStyledTable(modelPaid);
        addTableListener(tblPaid);
        JPanel pnlPaid = createSectionPanel("Siap Diproses (Lunas/Debit)", tblPaid, CafeColors.STATUS_PAID);

        pnlIncoming.add(pnlUnpaid);
        pnlIncoming.add(pnlPaid);

        // --- TAB 2: MONITORING ---
        JPanel pnlMonitoring = new JPanel(new BorderLayout());
        pnlMonitoring.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMonitoring.setBackground(CafeColors.BACKGROUND);

        modelProcessing = createTableModel();
        tblProcessing = createStyledTable(modelProcessing);
        addTableListener(tblProcessing);
        JPanel pnlProcContent = createSectionPanel("Sedang Diproses di Dapur", tblProcessing,
                CafeColors.STATUS_PROCESS);
        pnlMonitoring.add(pnlProcContent, BorderLayout.CENTER);

        // --- TAB 3: RIWAYAT ---
        JPanel pnlHistory = new JPanel(new BorderLayout());
        pnlHistory.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlHistory.setBackground(CafeColors.BACKGROUND);

        modelCompleted = createTableModel();
        tblCompleted = createStyledTable(modelCompleted);
        addTableListener(tblCompleted);
        JPanel pnlHistContent = createSectionPanel("Riwayat Pesanan Selesai", tblCompleted, CafeColors.STATUS_DONE);
        pnlHistory.add(pnlHistContent, BorderLayout.CENTER);

        tabbedPane.addTab("Pesanan Masuk", pnlIncoming);
        tabbedPane.addTab("Monitoring", pnlMonitoring);
        tabbedPane.addTab("Riwayat", pnlHistory);

        add(tabbedPane, BorderLayout.CENTER);
    }

    // =========================================
    // HELPER METHODS
    // =========================================

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setUI(new BasicButtonUI()); // FIX: Matikan style Windows agar warna muncul
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    private JPanel createSectionPanel(String title, JTable table, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel lblHeader = new JLabel(title);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(Color.WHITE);
        lblHeader.setForeground(accentColor);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, accentColor),
                new EmptyBorder(10, 15, 10, 15)));

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID Order", "Nama Customer", "Meja", "Total Harga", "Waktu" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(CafeColors.TABLE_GRID);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(CafeColors.TABLE_SELECTION);
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(CafeColors.TABLE_HEADER_BG);
        header.setForeground(CafeColors.TABLE_HEADER_TEXT);
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer paddedRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        };

        table.setDefaultRenderer(Object.class, paddedRenderer);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Meja
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Waktu

        return table;
    }

    private void addTableListener(JTable table) {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row == -1)
                    return;

                Object idObj = table.getValueAt(row, 0);
                String orderIdStr = idObj.toString();

                Order order = controller.getOrderDetail(orderIdStr);

                if (order != null) {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CashierDashboard.this);
                    OrderDetailDialog dialog = new OrderDetailDialog(parentFrame, controller, order);
                    dialog.setVisible(true);

                    if (dialog.isOrderUpdated()) {
                        refreshData();
                    }
                }
            }
        });
    }

    public void refreshData() {
        modelUnpaid.setRowCount(0);
        modelPaid.setRowCount(0);
        modelProcessing.setRowCount(0);
        modelCompleted.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        List<Order> unpaidOrders = controller.getUnpaidPendingOrders();
        for (Order o : unpaidOrders)
            modelUnpaid.addRow(createRowData(o, formatter));

        List<Order> paidOrders = controller.getPaidPendingOrders();
        for (Order o : paidOrders)
            modelPaid.addRow(createRowData(o, formatter));

        List<Order> processingOrders = controller.getProcessingOrders();
        for (Order o : processingOrders)
            modelProcessing.addRow(createRowData(o, formatter));

        List<Order> completedOrders = controller.getCompletedOrders();
        for (Order o : completedOrders)
            modelCompleted.addRow(createRowData(o, formatter));
    }

    private Object[] createRowData(Order o, DateTimeFormatter formatter) {
        return new Object[] {
                o.getOrderId(),
                o.getCustomerName(),
                o.getTableNumber(),
                "Rp " + (int) o.getTotalAmount(),
                o.getOrderTime().format(formatter)
        };
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        JFrame frame = new JFrame("Test Cashier System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.add(new CashierDashboard());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}