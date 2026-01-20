package com.example.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;

    public PaymentService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    public Order createRazorpayOrder(double amount) throws RazorpayException {

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100);
        options.put("currency", "INR");
        options.put("receipt", UUID.randomUUID().toString());

        return razorpayClient.orders.create(options);
    }
}