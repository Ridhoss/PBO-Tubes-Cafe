package app.controller;

import app.services.DummyOrdersDao;
import models.Order;
import java.util.List;
import java.util.stream.Collectors;

public class CashierController {

    private DummyOrdersDao ordersDao;

    public CashierController() {
        this.ordersDao = DummyOrdersDao.getInstance();
    }

    // --- LOGIC UNTUK TAB 1: PESANAN MASUK ---

    /**
     * Mengambil pesanan PENDING yang BELUM LUNAS (Biasanya Cash).
     * Ditampilkan di Tabel Kiri (Merah/Kuning).
     */
    public List<Order> getUnpaidPendingOrders() {
        return ordersDao.getAllOrders().stream()
                .filter(o -> o.getOrderStatus().equals("PENDING")) // Hanya yang baru masuk
                .filter(o -> !o.isPaid())                          // Hanya yang BELUM bayar
                .collect(Collectors.toList());
    }

    /**
     * Mengambil pesanan PENDING yang SUDAH LUNAS (Biasanya Debit).
     * Ditampilkan di Tabel Kanan (Hijau).
     */
    public List<Order> getPaidPendingOrders() {
        return ordersDao.getAllOrders().stream()
                .filter(o -> o.getOrderStatus().equals("PENDING")) // Hanya yang baru masuk
                .filter(o -> o.isPaid())                           // Hanya yang SUDAH bayar
                .collect(Collectors.toList());
    }

    // --- LOGIC UNTUK TAB 2: MONITORING ---

    /**
     * Mengambil pesanan yang sedang diproses atau selesai (Bukan Pending).
     */
    public List<Order> getProcessedOrders() {
        return ordersDao.getAllOrders().stream()
                .filter(o -> !o.getOrderStatus().equals("PENDING")) // Selain pending
                .collect(Collectors.toList());
    }

    // --- LOGIC AKSI (BUTTONS) ---

    /**
     * Menandai pesanan sebagai LUNAS.
     * Dipanggil ketika Scan Barcode berhasil atau Kasir terima uang cash.
     */
    public boolean markOrderAsPaid(String orderId) {
        Order order = ordersDao.findOrderByID(orderId);
        if (order != null) {
            order.setPaid(true);
            return true;
        }
        return false;
    }

    /**
     * Mengubah status pesanan menjadi PROCESSING (Approve).
     * Syarat: Pesanan harus sudah LUNAS.
     */
    public boolean approveOrder(String orderId) {
        Order order = ordersDao.findOrderByID(orderId);
        
        // Validasi: Pesanan harus ada
        if (order == null) return false;

        // Validasi: Pesanan Cash harus lunas dulu sebelum di-approve
        if (!order.isPaid()) {
            System.out.println("Gagal Approve: Pesanan belum dibayar!");
            return false;
        }

        // Ubah status
        order.setOrderStatus("PROCESSING");
        return true;
    }
    
    /**
     * Mencari pesanan spesifik (Utility untuk detail view)
     */
    public Order getOrderDetail(String orderId) {
        return ordersDao.findOrderByID(orderId);
    }

    /**
     * Mengubah status pesanan menjadi COMPLETED (Selesai).
     * Dipanggil ketika pesanan sudah disajikan/diambil.
     */
    public boolean completeOrder(String orderId) {
        Order order = ordersDao.findOrderByID(orderId);
        
        if (order != null && order.getOrderStatus().equals("PROCESSING")) {
            order.setOrderStatus("COMPLETED");
            return true;
        }
        return false;
    }
}