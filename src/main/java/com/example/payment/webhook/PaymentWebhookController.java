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

        Map<String, Object> payloadMap =
                (Map<String, Object>) payload.get("payload");

        Map<String, Object> paymentMap =
                (Map<String, Object>) payloadMap.get("payment");

        Map<String, Object> paymentEntity =
                (Map<String, Object>) paymentMap.get("entity");

        String paymentId = (String) paymentEntity.get("id");
        String status = (String) paymentEntity.get("status");
        String razorpayOrderId = (String) paymentEntity.get("order_id");

        Payment payment =
                paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElse(null);

        if (payment != null && status.equals("captured")) {

            payment.setStatus("SUCCESS");
            payment.setRazorpayPaymentId(paymentId);
            paymentRepository.save(payment);

            Order order =
                    orderRepository.findById(payment.getOrderId()).orElse(null);

            if (order != null) {
                order.setStatus("PAID");
                orderRepository.save(order);
            }
        }
    }
}
