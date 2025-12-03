// package app.services;

// import models.Order;
// import models.OrderItem;
// import models.Product;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// public class DummyOrdersDao {

// private static DummyOrdersDao instance;
// private List<Order> orders;

// private DummyOrdersDao() {
// orders = new ArrayList<>();
// generateDummyData();
// }

// public static DummyOrdersDao getInstance() {
// if (instance == null) {
// instance = new DummyOrdersDao();
// }
// return instance;
// }

// // Generate data pesanan contoh
// private void generateDummyData() {
// // Produk Dummy (Menggunakan class Product yang sudah ada di project kamu)
// // Pastikan constructor Product sesuai. Jika error, sesuaikan parameter new
// // Product(...)
// Product kopi = new Product(1, 1, "Kopi Susu Gula Aren", "Kopi enak", 18000,
// 10000, 100, true, null, null, null);
// Product snack = new Product(2, 2, "Kentang Goreng", "Gurih", 15000, 8000,
// 100, true, null, null, null);
// Product meal = new Product(3, 3, "Nasi Goreng", "Spesial", 25000, 15000, 100,
// true, null, null, null);

// // --- ORDER 1: CASH (Belum Lunas) ---
// // Kasus: Customer pesan, dapat barcode, belum bayar.
// Order order1 = new Order("Budi Santoso", "05", "CASH");
// order1.addItem(new OrderItem(kopi, 2, "Less Sugar"));
// order1.addItem(new OrderItem(snack, 1, ""));
// orders.add(order1);

// // --- ORDER 2: DEBIT (Sudah Lunas) ---
// // Kasus: Customer pesan via app, bayar debit, status PENDING (Menunggu
// Approval
// // Kasir)
// Order order2 = new Order("Siti Aminah", "02", "DEBIT");
// order2.addItem(new OrderItem(meal, 1, "Pedas Level 3"));
// order2.addItem(new OrderItem(kopi, 1, ""));
// orders.add(order2);

// // --- ORDER 3: CASH (Belum Lunas) ---
// Order order3 = new Order("Rudi Hartono", "10", "CASH");
// order3.addItem(new OrderItem(kopi, 1, "Normal"));
// orders.add(order3);
// }

// // --- Methods untuk Logika Kasir ---

// // Mengambil semua order
// public List<Order> getAllOrders() {
// return orders;
// }

// // Mengambil order yang BELUM diproses (Pending)
// // Ini nanti untuk Tab 1 (Pesanan Masuk)
// public List<Order> getPendingOrders() {
// return orders.stream()
// .filter(o -> o.getOrderStatus().equals("PENDING"))
// .collect(Collectors.toList());
// }

// // Mengambil order yang SUDAH diproses (Approved/Completed)
// // Ini nanti untuk Tab 2 (Monitoring)
// public List<Order> getProcessedOrders() {
// return orders.stream()
// .filter(o -> !o.getOrderStatus().equals("PENDING"))
// .collect(Collectors.toList());
// }

// // Cari order berdasarkan Order ID (Untuk fitur Scan Barcode)
// public Order findOrderByID(String orderId) {
// return orders.stream()
// .filter(o -> o.getOrderId().equalsIgnoreCase(orderId))
// .findFirst()
// .orElse(null);
// }
// }