package com.tp.service;

import com.tp.client.UserClient; // NEW: Feign Client import
import com.tp.dto.OrderWithUserResponseDTO;
import com.tp.dto.UserResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tp.dto.OrderRequestDTO;
import com.tp.dto.OrderResponseDTO;
import com.tp.entity.Order;
import com.tp.repository.OrderRepository;
//import org.springframework.web.client.RestTemplate; // COMMENTED: RestTemplate import

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
//    private final RestTemplate restTemplate;
private final UserClient userClient;

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getUserId()
        );
    }

    private Order mapToEntity(OrderRequestDTO dto) {
        Order order = new Order();
        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setPrice(dto.getPrice());
        order.setUserId(dto.getUserId());
        return order;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        Order order = mapToEntity(requestDTO);
        Order savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO requestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setProductName(requestDTO.getProductName());
        order.setQuantity(requestDTO.getQuantity());
        order.setPrice(requestDTO.getPrice());
        order.setUserId(requestDTO.getUserId());

        Order updatedOrder = orderRepository.save(order);
        return mapToResponseDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    @Override
    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "fallbackGetOrderWithUserDetails")
    @Retry(name = "userServiceRetry")
    public OrderWithUserResponseDTO getOrderWithUserDetails(Long orderId) {
        System.out.println("Calling User Service for orderId: " + orderId);

        // 1. Fetch order from the local database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // 2. Call User Service via OpenFeign
        UserResponseDTO userResponse = userClient.getUserById(order.getUserId());

        // 3. Assemble and return the combined response
        return new OrderWithUserResponseDTO(
                order.getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                userResponse
        );
    }

    public OrderWithUserResponseDTO fallbackGetOrderWithUserDetails(Long orderId, Throwable throwable) {
        System.err.println("Circuit Breaker / Fallback triggered for orderId: "
                + orderId + ". Reason: " + throwable.getMessage());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        UserResponseDTO fallbackUser = new UserResponseDTO(
                order.getUserId(),
                "User details temporarily unavailable",
                "N/A"
        );

        return new OrderWithUserResponseDTO(
                order.getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                fallbackUser
        );
    }
}