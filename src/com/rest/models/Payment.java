package com.rest.models;

import com.rest.payment.PaymentMethod;
import com.rest.payment.PaymentStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment {
    private int id;
    private int orderId;
    private double billAmount;
    private double amountPaid;
    private double changeReturned;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionReference;
    private LocalDateTime paymentTime;

    public Payment(int id, int orderId, double billAmount, double amountPaid, double changeReturned,
                   PaymentMethod method, PaymentStatus status, String transactionReference) {
        this.id = id;
        this.orderId = orderId;
        this.billAmount = billAmount;
        this.amountPaid = amountPaid;
        this.changeReturned = changeReturned;
        this.method = method;
        this.status = status;
        this.transactionReference = transactionReference;
        this.paymentTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getChangeReturned() {
        return changeReturned;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getFormattedReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("                PAYMENT RECEIPT                   \n");
        sb.append("==================================================\n");
        sb.append(String.format("Receipt ID      : #%d\n", id));
        sb.append(String.format("Order ID        : #%d\n", orderId));
        sb.append(String.format("Date & Time     : %s\n", paymentTime.format(formatter)));
        sb.append(String.format("Payment Method  : %s\n", method));
        sb.append(String.format("Reference / Note: %s\n", transactionReference));
        sb.append(String.format("Payment Status  : %s\n", status));
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("Bill Amount     : $%.2f\n", billAmount));
        sb.append(String.format("Amount Paid     : $%.2f\n", amountPaid));
        if (changeReturned > 0) {
            sb.append(String.format("Change Returned : $%.2f\n", changeReturned));
        }
        sb.append("==================================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Payment #%d | Order #%d | Method: %-11s | Amount: $%-7.2f | Status: %-7s | %s",
                id, orderId, method, billAmount, status, paymentTime.format(formatter));
    }
}
