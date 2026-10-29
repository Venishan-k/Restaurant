package com.rest.CLI;

import com.rest.models.Order;
import com.rest.models.Payment;
import com.rest.payment.PaymentMethod;
import com.rest.service.OrderService;
import com.rest.service.PaymentService;
import java.util.List;
import java.util.Scanner;

public class PaymentCLI {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final Scanner scanner;

    public PaymentCLI(PaymentService paymentService, OrderService orderService, Scanner scanner) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.scanner = scanner;
    }

    public void start() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== RESTAURANT PAYMENT MANAGEMENT ===");
            System.out.println("1. Process Payment for Order");
            System.out.println("2. View Payment Receipt by Order ID");
            System.out.println("3. View All Payment Transactions");
            System.out.println("4. View Total Revenue Summary");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleProcessPayment();
                    break;
                case "2":
                    handleViewReceipt();
                    break;
                case "3":
                    handleViewAllPayments();
                    break;
                case "4":
                    handleRevenueSummary();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select an option between 0 and 4.");
            }
        }
    }

    private void handleProcessPayment() {
        try {
            System.out.print("Enter Order ID to pay for: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());

            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order #" + orderId + " not found.");
                return;
            }

            double total = order.getTotalAmount();
            System.out.printf("Order #%d Total: $%.2f%n", orderId, total);

            if (total <= 0) {
                System.out.println("This order has no billable items.");
                return;
            }

            System.out.println("Select Payment Method:");
            System.out.println("1. Cash");
            System.out.println("2. Credit Card");
            System.out.println("3. Debit Card");
            System.out.println("4. UPI / Online");
            System.out.print("Enter method (1-4): ");
            String methodChoice = scanner.nextLine().trim();

            Payment payment = null;
            switch (methodChoice) {
                case "1":
                    System.out.print("Enter Cash Tendered ($): ");
                    double tendered = Double.parseDouble(scanner.nextLine().trim());
                    payment = paymentService.processCashPayment(orderId, tendered);
                    break;
                case "2":
                    System.out.print("Enter 16-digit Card Number: ");
                    String ccNumber = scanner.nextLine().trim();
                    payment = paymentService.processCardPayment(orderId, ccNumber, PaymentMethod.CREDIT_CARD);
                    break;
                case "3":
                    System.out.print("Enter 16-digit Card Number: ");
                    String dcNumber = scanner.nextLine().trim();
                    payment = paymentService.processCardPayment(orderId, dcNumber, PaymentMethod.DEBIT_CARD);
                    break;
                case "4":
                    System.out.print("Enter UPI ID (e.g. customer@okaxis): ");
                    String upiId = scanner.nextLine().trim();
                    payment = paymentService.processUpiPayment(orderId, upiId);
                    break;
                default:
                    System.out.println("Invalid payment method selected.");
                    return;
            }

            if (payment != null) {
                System.out.println("\nPayment processed successfully!");
                System.out.println(payment.getFormattedReceipt());
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Numeric input required.");
        } catch (Exception e) {
            System.out.println("Payment Failed: " + e.getMessage());
        }
    }

    private void handleViewReceipt() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            Payment payment = paymentService.getPaymentByOrderId(orderId);
            if (payment == null) {
                System.out.println("No payment found for Order #" + orderId + ".");
            } else {
                System.out.println("\n" + payment.getFormattedReceipt());
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Valid numeric order ID required.");
        }
    }

    private void handleViewAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        System.out.println("\n=== ALL PAYMENT TRANSACTIONS ===");
        if (payments.isEmpty()) {
            System.out.println("No payments recorded yet.");
            return;
        }
        for (Payment p : payments) {
            System.out.println(p);
        }
    }

    private void handleRevenueSummary() {
        double totalRevenue = paymentService.getTotalRevenue();
        int totalTransactions = paymentService.getAllPayments().size();
        System.out.println("\n=================================");
        System.out.println("      TOTAL REVENUE SUMMARY      ");
        System.out.println("=================================");
        System.out.printf("Total Successful Payments: %d%n", totalTransactions);
        System.out.printf("Total Revenue Collected  : $%.2f%n", totalRevenue);
        System.out.println("=================================");
    }
}
