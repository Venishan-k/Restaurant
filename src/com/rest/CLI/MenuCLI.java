package com.rest.CLI;

import com.rest.models.MenuItem;
import com.rest.service.MenuItemService;
import java.util.List;
import java.util.Scanner;

public class MenuCLI {
    private final MenuItemService service;
    private final Scanner scanner;

    public MenuCLI(MenuItemService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU ITEM MANAGEMENT ===");
            System.out.println("1. View Full Menu");
            System.out.println("2. Add New Menu Item");
            System.out.println("3. Remove Menu Item");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    displayMenu();
                    break;
                case "2":
                    addNewItem();
                    break;
                case "3":
                    removeItem();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void displayMenu() {
        List<MenuItem> items = service.getAllMenuItems();
        System.out.println("\n=== RESTAURANT MENU ===");
        if (items.isEmpty()) {
            System.out.println("Menu is currently empty.");
            return;
        }
        for (MenuItem item : items) {
            System.out.println(item);
        }
    }

    private void addNewItem() {
        try {
            System.out.print("Enter item name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter category (e.g. Appetizer, Main Course, Dessert, Beverage): ");
            String category = scanner.nextLine().trim();
            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            MenuItem item = service.addMenuItem(name, category, price);
            System.out.println("Added: " + item.getName() + " with ID: " + item.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid price entered.");
        }
    }

    private void removeItem() {
        try {
            System.out.print("Enter menu item ID to remove: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            boolean removed = service.removeMenuItem(id);
            if (removed) {
                System.out.println("Menu item removed successfully.");
            } else {
                System.out.println("Menu item not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID entered.");
        }
    }
}
