package com.rest.repository;

import com.rest.models.Payment;
import com.rest.payment.PaymentStatus;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private final List<Payment> payments = new ArrayList<>();
    private int nextPaymentId = 1001;

    public int getNextPaymentId() {
        return nextPaymentId++;
    }

    public void save(Payment payment) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId() == payment.getId()) {
                payments.set(i, payment);
                return;
            }
        }
        payments.add(payment);
    }

    public Payment findById(int id) {
        for (Payment payment : payments) {
            if (payment.getId() == id) {
                return payment;
            }
        }
        return null;
    }

    public Payment findByOrderId(int orderId) {
        for (Payment payment : payments) {
            if (payment.getOrderId() == orderId) {
                return payment;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }

    public double calculateTotalRevenue() {
        double total = 0.0;
        for (Payment payment : payments) {
            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                total += payment.getBillAmount();
            }
        }
        return total;
    }
}
