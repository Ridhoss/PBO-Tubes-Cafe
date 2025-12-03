package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId; // Ubah String ke int (sesuai SERIAL DB)
    private String customerName;
    private String tableNumber;
    private LocalDateTime orderTime;
    private List<OrderItem> items;
    private double totalAmount;

    // Status dari Database
    private String paymentStatus; // 'pending', 'paid', 'refunded'
    private String orderStatus; // 'pending', 'processing', 'completed'
    private String paymentMethod; // Akan diisi jika status 'paid'

    public Order() {
        this.items = new ArrayList<>();
    }

    // Constructor lengkap untuk mapping dari DB
    public Order(int orderId, String customerName, String tableNumber, double totalAmount,
            String paymentStatus, String orderStatus, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    // --- Logic Helper untuk UI ---

    // UI butuh tahu metode bayar untuk memisahkan tabel
    public String getPaymentTypeForUI() {
        if ("paid".equalsIgnoreCase(paymentStatus)) {
            // Jika sudah bayar, return method aslinya (misal DEBIT) atau default
            return paymentMethod != null ? paymentMethod : "CASHLESS";
        }
        return "CASH"; // Default kalau pending dianggap Cash (Bayar di kasir)
    }

    public boolean isPaid() {
        return "paid".equalsIgnoreCase(paymentStatus);
    }

    // --- Getters & Setters Standard ---
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}