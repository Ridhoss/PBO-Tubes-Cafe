package app.controller;

import app.services.OrdersDao;
import models.Order;
import java.util.List;
import java.util.stream.Collectors;

public class CashierController {

    private OrdersDao ordersDao; 

    public CashierController() {
        this.ordersDao = new OrdersDao();
    }

    // --- TAB 1: PESANAN MASUK (Tetap) ---

    public List<Order> getUnpaidPendingOrders() {
        return ordersDao.getOrders(false).stream()
                .filter(o -> !o.isPaid()) 
                .collect(Collectors.toList());
    }

    public List<Order> getPaidPendingOrders() {
        return ordersDao.getOrders(false).stream()
                .filter(o -> o.isPaid())
                .collect(Collectors.toList());
    }

    // --- TAB 2: MONITORING (UPDATE: DIPISAH) ---

    /**
     * Mengambil pesanan yang SEDANG DIPROSES (Processing).
     * Untuk Tabel Atas di Tab Monitoring.
     */
    public List<Order> getProcessingOrders() {
        return ordersDao.getOrders(true).stream()
                .filter(o -> "processing".equalsIgnoreCase(o.getOrderStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Mengambil pesanan yang SUDAH SELESAI (Completed/Done).
     * Untuk Tabel Bawah di Tab Monitoring.
     */
    public List<Order> getCompletedOrders() {
        return ordersDao.getOrders(true).stream()
                .filter(o -> "completed".equalsIgnoreCase(o.getOrderStatus()) || "done".equalsIgnoreCase(o.getOrderStatus()))
                .collect(Collectors.toList());
    }

    // --- AKSI (Tetap) ---

    public boolean markOrderAsPaid(int orderId, double amountPaid) {
        return ordersDao.processCashPayment(orderId, amountPaid);
    }

    public boolean approveOrder(int orderId) {
        Order order = ordersDao.getOrderById(orderId);
        if (order == null) return false;

        if (!order.isPaid()) {
            System.out.println("Gagal: Belum dibayar");
            return false;
        }
        
        return ordersDao.updateOrderStatus(orderId, "processing");
    }
    
    public boolean completeOrder(int orderId) {
        return ordersDao.updateOrderStatus(orderId, "completed");
    }

    public Order getOrderDetail(String orderIdStr) {
        try {
            int id = Integer.parseInt(orderIdStr);
            return ordersDao.getOrderById(id);
        } catch (NumberFormatException e) {
            return null; 
        }
    }
}