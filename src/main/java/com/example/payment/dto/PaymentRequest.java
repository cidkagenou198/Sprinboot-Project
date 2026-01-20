package com.example.payment.dto;

public class PaymentRequest {

    private String orderId;
    private double amount;

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }
}
