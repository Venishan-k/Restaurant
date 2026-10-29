package com.rest;

import com.rest.CLI.MenuCLI;
import com.rest.CLI.OrderCLI;
import com.rest.CLI.TableCLI;
import com.rest.repository.MenuItemRepository;
import com.rest.repository.OrderRepository;
import com.rest.repository.TableRepository;
import com.rest.service.MenuItemService;
import com.rest.service.OrderService;
import com.rest.service.TableService;
import com.rest.CLI.PaymentCLI;
import com.rest.repository.PaymentRepository;
import com.rest.service.PaymentService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Shared Repositories
        TableRepository tableRepository = new TableRepository();
        MenuItemRepository menuItemRepository = new MenuItemRepository();
        OrderRepository orderRepository = new OrderRepository();
        PaymentRepository paymentRepository = new PaymentRepository();

        // Services
        TableService tableService = new TableService(tableRepository);
        MenuItemService menuItemService = new MenuItemService(menuItemRepository);
        OrderService orderService = new OrderService(orderRepository, tableRepository, menuItemRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, orderService);

        Scanner scanner = new Scanner(System.in);
        TableCLI tableCLI = new TableCLI(tableService, scanner);
        MenuCLI menuCLI = new MenuCLI(menuItemService, scanner);
        OrderCLI orderCLI = new OrderCLI(orderService, menuItemService, scanner);
        PaymentCLI paymentCLI = new PaymentCLI(paymentService, orderService, scanner);

        boolean running = true;
        System.out.println("==================================================");
        System.out.println("      WELCOME TO RESTAURANT MANAGEMENT SYSTEM     ");
        System.out.println("==================================================");

        while (running) {
            System.out.println("\nMAIN MENU:");
            System.out.println("1. Table Management");
            System.out.println("2. Menu Item Management");
            System.out.println("3. Order Management");
            System.out.println("4. Payment Management");
            System.out.println("0. Exit System");
            System.out.print("Please enter your choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    tableCLI.start();
                    break;
                case "2":
                    menuCLI.start();
                    break;
                case "3":
                    orderCLI.start();
                    break;
                case "4":
                    paymentCLI.start();
                    break;
                case "0":
                    System.out.println("\nThank you for using Restaurant Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option between 0 and 4.");
            }
        }

        scanner.close();
    }
}