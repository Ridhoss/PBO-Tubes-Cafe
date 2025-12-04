package app.controller;

import app.services.OrderDao;
import java.util.ArrayList;
import models.Order;
import java.util.List;
import java.util.stream.Collectors;

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

    OrderController ordercontroller = OrderController.getInstance();

    // --- TAB 1: PESANAN MASUK (Tetap) ---
    public List<Order> getUnpaidPendingOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            if ("pending".equalsIgnoreCase(o.getOrder_status())
                    && !"paid".equalsIgnoreCase(o.getPayment_status())) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Order> getPaidPendingOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            if ("pending".equalsIgnoreCase(o.getOrder_status())
                    && "paid".equalsIgnoreCase(o.getPayment_status())) {
                result.add(o);
            }
        }
        return result;
    }

    // --------------------------------------------------
    // TAB 2 — Monitoring
    // --------------------------------------------------
    public List<Order> getProcessingOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            if ("processing".equalsIgnoreCase(o.getOrder_status())) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Order> getCompletedOrders() throws Exception {
        List<Order> data = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (Order o : data) {
            if ("completed".equalsIgnoreCase(o.getOrder_status())
                    || "done".equalsIgnoreCase(o.getOrder_status())) {
                result.add(o);
            }
        }
        return result;
    }

    // --------------------------------------------------
    // AKSI
    // --------------------------------------------------
    public boolean markOrderAsPaid(int orderId, double amountPaid) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new Exception("Order ID " + orderId + " tidak ditemukan");
        }

        // Jika ingin menyimpan nilai pembayaran, harus sudah tersedia field di database (final_amount, dll)
        // Untuk sistem minimal: cukup ubah payment_status menjadi PAID
        order.setPayment_status("paid");
        orderDao.update(order);
        return true;
    }

    public boolean approveOrder(int orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new Exception("Order ID " + orderId + " tidak ditemukan");
        }

        if (!"paid".equalsIgnoreCase(order.getPayment_status())) {
            throw new Exception("Gagal: Order belum dibayar");
        }

        order.setOrder_status("processing");
        orderDao.update(order);
        return true;
    }

    public boolean completeOrder(int orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new Exception("Order ID " + orderId + " tidak ditemukan");
        }

        order.setOrder_status("completed");
        orderDao.update(order);
        return true;
    }

    public Order getOrderDetail(String orderIdStr) throws Exception {
        try {
            int id = Integer.parseInt(orderIdStr);
            return orderDao.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
