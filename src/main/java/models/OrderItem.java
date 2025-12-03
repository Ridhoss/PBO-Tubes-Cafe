package models;

public class OrderItem {
    private Product product;
    private int quantity;
    private String notes; // Contoh: "Less sugar", "Pedas"
    private double subtotal;

    public OrderItem(Product product, int quantity, String notes) {
        this.product = product;
        this.quantity = quantity;
        this.notes = notes;
        this.subtotal = product.getPrice() * quantity;
    }

    // Getters
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public String getNotes() { return notes; }
    public double getSubtotal() { return subtotal; }
    
    // Helper untuk menampilkan nama produk + notes di UI nanti
    public String getItemSummary() {
        return product.getProduct_name() + (notes.isEmpty() ? "" : " (" + notes + ")");
    }
}