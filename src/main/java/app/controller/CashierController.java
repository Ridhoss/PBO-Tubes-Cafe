package app.controller;

import app.services.OrderDao;
import java.util.ArrayList;
import models.Order;
import java.util.List;

public class CashierController {

    private static CashierController instance;
    private final OrderDao orderDao;

    private CashierController() {
        orderDao = new OrderDao();
    }

    public static synchronized CashierController getInstance() {
        if (instance == null) {
            instance = new CashierController();
        }
        return instance;
    }

    // --- HELPER: PATCH DATA YANG NULL DARI DAO ---
    // Karena OrderDao tidak memetakan total_amount, kita isi manual dari final_amount
    // agar tidak null saat ditampilkan atau di-update.
    private Order validateOrderData(Order o) {
        if (o != null) {
            if (o.getTotal_amount() == null && o.getFinal_amount() != null) {
                o.setTotal_amount(o.getFinal_amount());
            }
            // Jika discount null, set ke 0 agar aman
            if (o.getDiscount() == null) {
                o.setDiscount(0);
            }
        }
        return o;
    }

    // --- TAB 1: PESANAN MASUK ---

    public List<Order> getUnpaidPendingOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            validateOrderData(o); // Patch data null
            
            // Filter: Status Waiting (App) atau pending (DB) DAN Belum Bayar
            boolean isPending = "Waiting".equalsIgnoreCase(o.getOrder_status()) 
                             || "pending".equalsIgnoreCase(o.getOrder_status());
            boolean isNotPaid = !"paid".equalsIgnoreCase(o.getPayment_status());

            if (isPending && isNotPaid) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Order> getPaidPendingOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            validateOrderData(o); // Patch data null
            
            // Filter: Status Waiting/pending DAN Sudah Bayar
            boolean isPending = "Waiting".equalsIgnoreCase(o.getOrder_status()) 
                             || "pending".equalsIgnoreCase(o.getOrder_status());
            boolean isPaid = "paid".equalsIgnoreCase(o.getPayment_status());

            if (isPending && isPaid) {
                result.add(o);
            }
        }
        return result;
    }

    // --- TAB 2: MONITORING ---
    public List<Order> getProcessingOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            validateOrderData(o);
            if ("processing".equalsIgnoreCase(o.getOrder_status())) {
                result.add(o);
            }
        }
        return result;
    }

    // --- TAB 3: RIWAYAT ---
    public List<Order> getCompletedOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            validateOrderData(o);
            if ("completed".equalsIgnoreCase(o.getOrder_status())
                    || "done".equalsIgnoreCase(o.getOrder_status())) {
                result.add(o);
            }
        }
        return result;
    }

    // --- AKSI (Update Status) ---
    
    public boolean markOrderAsPaid(int orderId, double amountPaid) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) throw new Exception("Order tidak ditemukan");
        
        validateOrderData(order); // PENTING: Patch sebelum update agar tidak NPE

        order.setPayment_status("paid");
        orderDao.update(order);
        return true;
    }

    public boolean approveOrder(int orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) throw new Exception("Order tidak ditemukan");
        validateOrderData(order);

        if (!"paid".equalsIgnoreCase(order.getPayment_status())) {
            throw new Exception("Gagal: Order belum dibayar");
        }

        order.setOrder_status("processing");
        orderDao.update(order);
        return true;
    }

    public boolean completeOrder(int orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) throw new Exception("Order tidak ditemukan");
        validateOrderData(order);

        order.setOrder_status("completed");
        orderDao.update(order);
        return true;
    }

    // Helper untuk Barcode Scanner
    public Order getOrderDetail(String searchKey) throws Exception {
        try {
            int id = Integer.parseInt(searchKey);
            Order o = orderDao.findById(id);
            return validateOrderData(o);
        } catch (NumberFormatException e) {
            // Fallback cari berdasarkan Order Code jika input bukan angka ID
            List<Order> all = orderDao.findAll();
            for(Order o : all) {
                if(o.getOrderCode() != null && o.getOrderCode().equalsIgnoreCase(searchKey)) {
                    return validateOrderData(o);
                }
            }
            return null;
        }
    }
}