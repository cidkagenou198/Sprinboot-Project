package com.example.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private RazorpayClient client;

    public PaymentService(RazorpayClient client) {
        this.client = client;
    }

    public Order createRazorpayOrder(double amount) throws RazorpayException {

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", UUID.randomUUID().toString());

        return client.orders.create(orderRequest);
    }
}
