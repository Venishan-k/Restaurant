package com.rest.repository;

import com.rest.models.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {
    private final List<MenuItem> menuItems = new ArrayList<>();

    public MenuItemRepository() {
        // Pre-populate menu with popular restaurant items
        menuItems.add(new MenuItem(1, "Garlic Bread", "Appetizer", 4.99));
        menuItems.add(new MenuItem(2, "Chicken Wings", "Appetizer", 8.99));
        menuItems.add(new MenuItem(3, "Caesar Salad", "Appetizer", 6.49));
        menuItems.add(new MenuItem(4, "Margherita Pizza", "Main Course", 12.99));
        menuItems.add(new MenuItem(5, "Cheeseburger Deluxe", "Main Course", 10.99));
        menuItems.add(new MenuItem(6, "Grilled Salmon", "Main Course", 16.99));
        menuItems.add(new MenuItem(7, "Penne Alfredo", "Main Course", 13.49));
        menuItems.add(new MenuItem(8, "Chocolate Lava Cake", "Dessert", 6.99));
        menuItems.add(new MenuItem(9, "Tiramisu", "Dessert", 5.99));
        menuItems.add(new MenuItem(10, "Iced Lemon Tea", "Beverage", 2.99));
        menuItems.add(new MenuItem(11, "Espresso", "Beverage", 3.49));
    }

    public List<MenuItem> findAll() {
        return new ArrayList<>(menuItems);
    }

    public MenuItem findById(int id) {
        for (MenuItem item : menuItems) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public boolean removeMenuItem(int id) {
        return menuItems.removeIf(item -> item.getId() == id);
    }

    public int getNextId() {
        int maxId = 0;
        for (MenuItem item : menuItems) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }
        return maxId + 1;
    }
}
