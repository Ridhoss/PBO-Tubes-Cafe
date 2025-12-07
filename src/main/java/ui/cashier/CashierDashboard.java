package ui.cashier;

import app.controller.CashierController;
import app.controller.OrderController;
import app.controller.UserController; // Integrasi User
import models.Order;
import models.User;
import ui.util.CafeColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
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

    private JTable tblUnpaid, tblPaid, tblProcessing, tblCompleted;
    private DefaultTableModel modelUnpaid, modelPaid, modelProcessing, modelCompleted;

    CashierController cashiercontroller = CashierController.getInstance();
    OrderController ordercontroller = OrderController.getInstance();
    UserController userController = UserController.getInstance(); // Controller User

    public CashierDashboard() {
        initComponents();
        refreshData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(CafeColors.BACKGROUND);

        // --- HEADER ---
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

        btnRefresh.addActionListener((ActionEvent e) -> {
            refreshData();
            JOptionPane.showMessageDialog(this, "Data diperbarui!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        btnScan.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            BarcodeScannerDialog scanner = new BarcodeScannerDialog(parentFrame, cashiercontroller);
            scanner.setVisible(true);
            if (scanner.isScanSuccess()) refreshData();
        });

        pnlActions.add(btnScan);
        pnlActions.add(btnRefresh);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlActions, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- TABS ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(CafeColors.BACKGROUND);

        // Tab 1: Pesanan Masuk
        JPanel pnlIncoming = new JPanel(new GridLayout(2, 1, 0, 20));
        pnlIncoming.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlIncoming.setBackground(CafeColors.BACKGROUND);

        modelUnpaid = createTableModel();
        tblUnpaid = createStyledTable(modelUnpaid);
        addTableListener(tblUnpaid);
        pnlIncoming.add(createSectionPanel("Menunggu Pembayaran (Cash)", tblUnpaid, CafeColors.STATUS_UNPAID));

        modelPaid = createTableModel();
        tblPaid = createStyledTable(modelPaid);
        addTableListener(tblPaid);
        pnlIncoming.add(createSectionPanel("Siap Diproses (Lunas/Debit)", tblPaid, CafeColors.STATUS_PAID));

        // Tab 2: Monitoring
        JPanel pnlMonitoring = new JPanel(new BorderLayout());
        pnlMonitoring.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMonitoring.setBackground(CafeColors.BACKGROUND);
        modelProcessing = createTableModel();
        tblProcessing = createStyledTable(modelProcessing);
        addTableListener(tblProcessing);
        pnlMonitoring.add(createSectionPanel("Sedang Diproses Dapur", tblProcessing, CafeColors.STATUS_PROCESS), BorderLayout.CENTER);

        // Tab 3: Riwayat
        JPanel pnlHistory = new JPanel(new BorderLayout());
        pnlHistory.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlHistory.setBackground(CafeColors.BACKGROUND);
        modelCompleted = createTableModel();
        tblCompleted = createStyledTable(modelCompleted);
        addTableListener(tblCompleted);
        pnlHistory.add(createSectionPanel("Riwayat Selesai", tblCompleted, CafeColors.STATUS_DONE), BorderLayout.CENTER);

        tabbedPane.addTab("Pesanan Masuk", pnlIncoming);
        tabbedPane.addTab("Monitoring", pnlMonitoring);
        tabbedPane.addTab("Riwayat", pnlHistory);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        try {
            modelUnpaid.setRowCount(0);
            modelPaid.setRowCount(0);
            modelProcessing.setRowCount(0);
            modelCompleted.setRowCount(0);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
            
            // Ambil data lewat CashierController (yang sudah di-patch validasi null-nya)
            List<Order> unpaid = cashiercontroller.getUnpaidPendingOrders();
            List<Order> paid = cashiercontroller.getPaidPendingOrders();
            List<Order> processing = cashiercontroller.getProcessingOrders();
            List<Order> completed = cashiercontroller.getCompletedOrders();

            for (Order o : unpaid) modelUnpaid.addRow(createRowData(o, formatter));
            for (Order o : paid) modelPaid.addRow(createRowData(o, formatter));
            for (Order o : processing) modelProcessing.addRow(createRowData(o, formatter));
            for (Order o : completed) modelCompleted.addRow(createRowData(o, formatter));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Object[] createRowData(Order o, DateTimeFormatter formatter) {
        // Integrasi: Ambil Nama Customer
        String customerName = "Guest";
        try {
            User u = userController.findById(o.getUser_id());
            if (u != null) customerName = u.getFull_name();
        } catch (Exception e) {}

        String timeStr = o.getOrder_date() != null ? o.getOrder_date().format(formatter) : "-";
        
        // PENTING: Gunakan getFinal_amount() agar tidak null
        return new Object[]{
            o.getOrder_id(),
            o.getOrderCode(),
            customerName,
            o.getTableCode(),
            "Rp " + (int) o.getFinal_amount(), 
            timeStr
        };
    }

    // --- Helper UI Methods ---
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setUI(new BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JPanel createSectionPanel(String title, JTable table, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        JLabel lbl = new JLabel(title);
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setForeground(color);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,2,0, color), new EmptyBorder(10,15,10,15)));
        p.add(lbl, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Kode", "Customer", "Meja", "Total", "Waktu"}) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(35);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(CafeColors.TABLE_HEADER_BG);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        t.setDefaultRenderer(Object.class, center);
        return t;
    }

    private void addTableListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row == -1) return;
                    try {
                        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                        // Ambil order lewat controller agar ter-patch datanya
                        Order order = cashiercontroller.getOrderDetail(String.valueOf(id));
                        if (order != null) {
                            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(CashierDashboard.this);
                            OrderDetailDialog dialog = new OrderDetailDialog(parent, cashiercontroller, ordercontroller, order);
                            dialog.setVisible(true);
                            if (dialog.isOrderUpdated()) refreshData();
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
            }
        });
    }
}