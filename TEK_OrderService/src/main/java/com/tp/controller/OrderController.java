package com.tp.controller;

import com.tp.dto.OrderWithUserResponseDTO;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tp.dto.OrderRequestDTO;
import com.tp.dto.OrderResponseDTO;
import com.tp.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO requestDTO) {
        return new ResponseEntity<>(orderService.createOrder(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderRequestDTO requestDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully.");
    }

    @GetMapping("/{id}/with-user")
    @RateLimiter(name = "orderRateLimiter", fallbackMethod = "rateLimitFallback")
    public ResponseEntity<?> getOrderWithUserDetails(@PathVariable Long id) {
        OrderWithUserResponseDTO response = orderService.getOrderWithUserDetails(id);
        return ResponseEntity.ok(response);
    }
    /**
     * Fallback triggered when the request exceeds the rate limit window
     */
    public ResponseEntity<?> rateLimitFallback(Long id, RequestNotPermitted ex) {
        System.err.println("Rate limit exceeded for orderId: " + id);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests! You have exceeded the limit of 2 requests per 10 seconds.");
    }
}