package com.example.payment.webhook;

import com.example.payment.model.Payment;
import com.example.payment.model.Order;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class PaymentWebhookController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/razorpay")
    public void handleWebhook(@RequestBody Map<String, Object> payload) {

        Map<String, Object> entity =
                (Map<String, Object>) payload.get("payload");

        Map<String, Object> payment =
                (Map<String, Object>) entity.get("payment");

        Map<String, Object> paymentEntity =
                (Map<String, Object>) payment.get("entity");

        String paymentId = (String) paymentEntity.get("id");
        String status = (String) paymentEntity.get("status");
        String orderId = (String) paymentEntity.get("order_id");

        System.out.println("Webhook orderId = " + orderId);
        System.out.println("Webhook status = " + status);

        Payment pay = paymentRepository.findByRazorpayOrderId(orderId).orElse(null);

        if (pay != null && status.equals("captured")) {
            pay.setStatus("SUCCESS");
            pay.setRazorpayPaymentId(paymentId);
            paymentRepository.save(pay);

            Order order = orderRepository.findById(pay.getOrderId()).orElse(null);

            if (order != null) {
                order.setStatus("PAID");
                orderRepository.save(order);
            }
        }
    }
}