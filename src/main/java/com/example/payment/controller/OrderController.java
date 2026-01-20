package com.example.payment.controller;

import com.example.payment.model.Order;
import com.example.payment.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Order saveOrder(@RequestBody Order orderRequest) {
        return repository.save(orderRequest);
    }

    @GetMapping("/{orderId}")
    public Order fetchOrderById(@PathVariable String orderId) {
        return repository.findById(orderId).orElse(null);
    }
}
