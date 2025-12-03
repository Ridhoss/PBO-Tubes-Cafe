package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private String orderId;
    private String customerName;
    private String tableNumber;
    private LocalDateTime orderTime;
    private List<OrderItem> items;
    private double totalAmount;
    
    // Status Pembayaran & Order
    private String paymentType; // "CASH" atau "DEBIT"
    private boolean isPaid;     // true = Lunas, false = Belum
    private String orderStatus; // "PENDING", "APPROVED", "COMPLETED"

    public Order(String customerName, String tableNumber, String paymentType) {
        // Generate ID unik pendek untuk Barcode (Misal: ORD-AB12)
        this.orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.paymentType = paymentType;
        this.orderTime = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.orderStatus = "PENDING";
        
        // Logika Status Pembayaran sesuai request
        // Jika Debit -> Anggap sudah lunas di sisi customer (tinggal approve kasir)
        // Jika Cash -> Belum lunas (harus ke kasir)
        if (paymentType.equalsIgnoreCase("DEBIT")) {
            this.isPaid = true;
        } else {
            this.isPaid = false;
        }
    }

    public void addItem(OrderItem item) {
        items.add(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        totalAmount = 0;
        for (OrderItem item : items) {
            totalAmount += item.getSubtotal();
        }
    }

    // --- Getters & Setters ---
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getTableNumber() { return tableNumber; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentType() { return paymentType; }
    public boolean isPaid() { return isPaid; }
    public String getOrderStatus() { return orderStatus; }

    public void setPaid(boolean paid) { isPaid = paid; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
}