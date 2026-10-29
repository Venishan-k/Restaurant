package com.rest.models;

public class OrderItem {
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public double getSubtotal() {
        return menuItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%-20s x %-2d @ $%-6.2f = $%.2f",
                menuItem.getName(), quantity, menuItem.getPrice(), getSubtotal());
    }
}
