package app.services;

import database.DBConnection;
import models.Order;
import models.OrderItem;
import models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDao {

    // --- REVISI: Koneksi dipanggil di luar try-with-resources agar tidak auto-close ---

    public List<Order> getOrders(boolean isProcessed) {
        List<Order> orders = new ArrayList<>();
        String sql;

        if (!isProcessed) {
            sql = "SELECT * FROM orders WHERE order_status = 'pending' ORDER BY order_date DESC";
        } else {
            sql = "SELECT * FROM orders WHERE order_status IN ('processing', 'completed', 'done') ORDER BY order_date DESC";
        }

        Connection conn = DBConnection.getInstance().getConnection(); // Ambil koneksi
        
        try (PreparedStatement ps = conn.prepareStatement(sql); // Hanya PS dan RS yang di-close otomatis
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = mapOrder(rs);
                if ("paid".equalsIgnoreCase(order.getPaymentStatus())) {
                    order.setPaymentMethod(getPaymentMethod(conn, order.getOrderId()));
                }
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        Connection conn = DBConnection.getInstance().getConnection(); // Ambil koneksi

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = mapOrder(rs);
                order.setItems(getOrderItems(conn, orderId));
                if (order.isPaid()) {
                    order.setPaymentMethod(getPaymentMethod(conn, orderId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean processCashPayment(int orderId, double amountPaid) {
        Connection conn = DBConnection.getInstance().getConnection();
        
        try {
            conn.setAutoCommit(false); // Mulai Transaksi

            // 1. Update status orders
            String updateSql = "UPDATE orders SET payment_status = 'paid' WHERE order_id = ?";
            try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                psUpdate.setInt(1, orderId);
                psUpdate.executeUpdate();
            }

            // 2. Hitung kembalian (Aman memanggil getOrderById karena koneksi tidak diclose di sana)
            Order o = getOrderById(orderId); 
            double change = amountPaid - o.getTotalAmount();

            // 3. Insert payments
            String insertSql = "INSERT INTO payments (order_id, payment_method, amount_paid, change_amount) VALUES (?, 'CASH', ?, ?)";
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setInt(1, orderId);
                psInsert.setBigDecimal(2, java.math.BigDecimal.valueOf(amountPaid));
                psInsert.setBigDecimal(3, java.math.BigDecimal.valueOf(change));
                psInsert.executeUpdate();
            }

            conn.commit(); // Simpan
            return true;

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (Exception ex) {}
            // Jangan close conn di sini karena Singleton
        }
    }

    // --- Helper Methods (Koneksi dipassing dari method pemanggil) ---

    private Order mapOrder(ResultSet rs) throws SQLException {
        return new Order(
            rs.getInt("order_id"),
            rs.getString("customer_name"),
            rs.getString("table_number"),
            rs.getBigDecimal("final_amount").doubleValue(),
            rs.getString("payment_status"),
            rs.getString("order_status"),
            rs.getTimestamp("order_date").toLocalDateTime()
        );
    }

    private List<OrderItem> getOrderItems(Connection conn, int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.product_name, p.price " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Product p = new Product();
                p.setProduct_name(rs.getString("product_name"));
                p.setPrice(rs.getBigDecimal("price").intValue());
                items.add(new OrderItem(p, rs.getInt("quantity"), rs.getString("notes")));
            }
        }
        return items;
    }

    private String getPaymentMethod(Connection conn, int orderId) {
        String sql = "SELECT payment_method FROM payments WHERE order_id = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("payment_method");
        } catch (Exception e) {}
        return "UNKNOWN";
    }
}