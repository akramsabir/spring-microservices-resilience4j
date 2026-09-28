package com.tp.service;

import com.tp.dto.OrderRequestDTO;
import com.tp.dto.OrderResponseDTO;
import com.tp.dto.OrderWithUserResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getAllOrders();

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    OrderResponseDTO updateOrder(Long id, OrderRequestDTO requestDTO);

    void deleteOrder(Long id);

    OrderWithUserResponseDTO getOrderWithUserDetails(Long orderId);
}