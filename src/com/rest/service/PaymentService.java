package com.rest.service;

import com.rest.models.Order;
import com.rest.models.Order.OrderStatus;
import com.rest.models.Payment;
import com.rest.payment.PaymentMethod;
import com.rest.payment.PaymentStatus;
import com.rest.repository.PaymentRepository;
import java.util.List;

public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    public Payment processCashPayment(int orderId, double cashTendered) {
        Order order = orderService.getOrderById(orderId);
        validateOrderForPayment(order, orderId);

        double billAmount = order.getTotalAmount();
        if (cashTendered < billAmount) {
            throw new IllegalArgumentException(String.format(
                    "Insufficient cash tendered. Bill is $%.2f, tendered $%.2f.", billAmount, cashTendered));
        }

        double change = cashTendered - billAmount;
        int paymentId = paymentRepository.getNextPaymentId();
        Payment payment = new Payment(
                paymentId,
                orderId,
                billAmount,
                cashTendered,
                change,
                PaymentMethod.CASH,
                PaymentStatus.SUCCESS,
                "CASH-" + paymentId
        );

        paymentRepository.save(payment);
        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
        return payment;
    }

    public Payment processCardPayment(int orderId, String cardNumber, PaymentMethod cardType) {
        Order order = orderService.getOrderById(orderId);
        validateOrderForPayment(order, orderId);

        if (cardNumber == null || cardNumber.trim().length() < 4) {
            throw new IllegalArgumentException("Invalid card number. At least 4 digits required.");
        }

        String cleanedCard = cardNumber.trim().replaceAll("\\s+", "");
        String last4 = cleanedCard.substring(cleanedCard.length() - 4);
        String maskedCard = "**** **** **** " + last4;

        double billAmount = order.getTotalAmount();
        int paymentId = paymentRepository.getNextPaymentId();
        PaymentMethod method = (cardType == PaymentMethod.DEBIT_CARD) ? PaymentMethod.DEBIT_CARD : PaymentMethod.CREDIT_CARD;

        Payment payment = new Payment(
                paymentId,
                orderId,
                billAmount,
                billAmount,
                0.0,
                method,
                PaymentStatus.SUCCESS,
                method + "-" + paymentId + " (" + maskedCard + ")"
        );

        paymentRepository.save(payment);
        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
        return payment;
    }

    public Payment processUpiPayment(int orderId, String upiId) {
        Order order = orderService.getOrderById(orderId);
        validateOrderForPayment(order, orderId);

        if (upiId == null || !upiId.contains("@")) {
            throw new IllegalArgumentException("Invalid UPI ID. Format should be username@bank.");
        }

        double billAmount = order.getTotalAmount();
        int paymentId = paymentRepository.getNextPaymentId();
        Payment payment = new Payment(
                paymentId,
                orderId,
                billAmount,
                billAmount,
                0.0,
                PaymentMethod.UPI,
                PaymentStatus.SUCCESS,
                "UPI-" + paymentId + " (" + upiId.trim() + ")"
        );

        paymentRepository.save(payment);
        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
        return payment;
    }

    private void validateOrderForPayment(Order order, int orderId) {
        if (order == null) {
            throw new IllegalArgumentException("Order #" + orderId + " does not exist.");
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Order #" + orderId + " has already been completed / paid.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order #" + orderId + " has been cancelled.");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Order #" + orderId + " has no items to pay for.");
        }
        Payment existing = paymentRepository.findByOrderId(orderId);
        if (existing != null && existing.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("A successful payment already exists for Order #" + orderId + ".");
        }
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id);
    }

    public Payment getPaymentByOrderId(int orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public double getTotalRevenue() {
        return paymentRepository.calculateTotalRevenue();
    }
}
