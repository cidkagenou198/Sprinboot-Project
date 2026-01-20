package com.example.payment.controller;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.service.PaymentService;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public PaymentController(PaymentRepository paymentRepository,
                             PaymentService paymentService) {
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) throws Exception {

        Order razorpayOrder = paymentService.createRazorpayOrder(request.getAmount());

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        payment.setAmount(request.getAmount());
        payment.setStatus("PENDING");

        paymentRepository.save(payment);

        return ResponseEntity.ok(Map.of(
                "orderId", request.getOrderId(),
                "amount", request.getAmount(),
                "status", "PENDING",
                "razorpayOrderId", razorpayOrder.get("id")
        ));
    }
    @GetMapping("/order/{orderId}")
    public List<Payment> getPayments(@PathVariable String orderId){
        return paymentRepository.findByOrderId(orderId);
    }
}