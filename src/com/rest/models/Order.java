package com.rest.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {

    public enum OrderStatus {
        PENDING,
        PREPARING,
        SERVED,
        COMPLETED,
        CANCELLED
    }

    private int id;
    private int tableId;
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDateTime orderTime;

    public Order(int id, int tableId) {
        this.id = id;
        this.tableId = tableId;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public int getTableId() {
        return tableId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        if (menuItem == null || quantity <= 0) {
            return;
        }
        for (OrderItem item : items) {
            if (item.getMenuItem().getId() == menuItem.getId()) {
                item.addQuantity(quantity);
                return;
            }
        }
        items.add(new OrderItem(menuItem, quantity));
    }

    public boolean removeItem(int menuItemId) {
        return items.removeIf(item -> item.getMenuItem().getId() == menuItemId);
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public String getFormattedBill() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append(String.format("                ORDER BILL #%d\n", id));
        sb.append("==================================================\n");
        sb.append(String.format("Table ID   : %d\n", tableId));
        sb.append(String.format("Date & Time: %s\n", orderTime.format(formatter)));
        sb.append(String.format("Status     : %s\n", status));
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-20s %-5s %-10s %-10s\n", "Item", "Qty", "Price", "Subtotal"));
        sb.append("--------------------------------------------------\n");

        if (items.isEmpty()) {
            sb.append("No items in this order.\n");
        } else {
            for (OrderItem item : items) {
                sb.append(String.format("%-20s %-5d $%-9.2f $%-9.2f\n",
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getMenuItem().getPrice(),
                        item.getSubtotal()));
            }
        }

        sb.append("--------------------------------------------------\n");
        sb.append(String.format("TOTAL AMOUNT:                       $%.2f\n", getTotalAmount()));
        sb.append("==================================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Order #%d | Table %d | Status: %-9s | Items: %d | Total: $%.2f | Time: %s",
                id, tableId, status, items.size(), getTotalAmount(), orderTime.format(formatter));
    }
}
