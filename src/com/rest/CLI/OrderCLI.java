package com.rest.CLI;

import com.rest.models.MenuItem;
import com.rest.models.Order;
import com.rest.models.Order.OrderStatus;
import com.rest.service.MenuItemService;
import com.rest.service.OrderService;
import java.util.List;
import java.util.Scanner;

public class OrderCLI {
    private final OrderService orderService;
    private final MenuItemService menuItemService;
    private final Scanner scanner;

    public OrderCLI(OrderService orderService, MenuItemService menuItemService, Scanner scanner) {
        this.orderService = orderService;
        this.menuItemService = menuItemService;
        this.scanner = scanner;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== RESTAURANT ORDER MANAGEMENT ===");
            System.out.println("1. Create New Order for Table");
            System.out.println("2. Add Item to Order");
            System.out.println("3. Remove Item from Order");
            System.out.println("4. View Order / Generate Bill");
            System.out.println("5. Update Order Status");
            System.out.println("6. View All Orders");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleCreateOrder();
                    break;
                case "2":
                    handleAddItem();
                    break;
                case "3":
                    handleRemoveItem();
                    break;
                case "4":
                    handleViewBill();
                    break;
                case "5":
                    handleUpdateStatus();
                    break;
                case "6":
                    handleViewAllOrders();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleCreateOrder() {
        try {
            System.out.print("Enter Table ID for new order: ");
            int tableId = Integer.parseInt(scanner.nextLine().trim());
            Order order = orderService.createOrder(tableId);
            System.out.println("Order created successfully! Assigned Order ID: #" + order.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid numeric table ID.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleAddItem() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());

            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order #" + orderId + " does not exist.");
                return;
            }

            System.out.println("\n--- Available Menu Items ---");
            List<MenuItem> menu = menuItemService.getAllMenuItems();
            for (MenuItem item : menu) {
                System.out.println(item);
            }

            System.out.print("Enter Menu Item ID to add: ");
            int menuItemId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            orderService.addItemToOrder(orderId, menuItemId, quantity);
            System.out.println("Item added to order successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric input.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleRemoveItem() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Menu Item ID to remove: ");
            int menuItemId = Integer.parseInt(scanner.nextLine().trim());

            boolean removed = orderService.removeItemFromOrder(orderId, menuItemId);
            if (removed) {
                System.out.println("Item removed from order successfully.");
            } else {
                System.out.println("Item not found in order or could not be removed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric input.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleViewBill() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            String bill = orderService.getBill(orderId);
            System.out.println("\n" + bill);
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid numeric order ID.");
        }
    }

    private void handleUpdateStatus() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order #" + orderId + " not found.");
                return;
            }

            System.out.println("Current status: " + order.getStatus());
            System.out.println("Choose new status:");
            System.out.println("1. PENDING");
            System.out.println("2. PREPARING");
            System.out.println("3. SERVED");
            System.out.println("4. COMPLETED (Closes order and frees table)");
            System.out.println("5. CANCELLED (Cancels order and frees table)");
            System.out.print("Enter choice (1-5): ");

            int choice = Integer.parseInt(scanner.nextLine().trim());
            OrderStatus newStatus = null;
            switch (choice) {
                case 1: newStatus = OrderStatus.PENDING; break;
                case 2: newStatus = OrderStatus.PREPARING; break;
                case 3: newStatus = OrderStatus.SERVED; break;
                case 4: newStatus = OrderStatus.COMPLETED; break;
                case 5: newStatus = OrderStatus.CANCELLED; break;
                default:
                    System.out.println("Invalid status option.");
                    return;
            }

            orderService.updateOrderStatus(orderId, newStatus);
            System.out.println("Order #" + orderId + " updated to status: " + newStatus);
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric input.");
        }
    }

    private void handleViewAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        System.out.println("\n=== ALL ORDERS ===");
        if (orders.isEmpty()) {
            System.out.println("No orders placed yet.");
            return;
        }
        for (Order o : orders) {
            System.out.println(o);
        }
    }
}
