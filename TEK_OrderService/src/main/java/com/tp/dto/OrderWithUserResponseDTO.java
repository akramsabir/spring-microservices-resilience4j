package com.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithUserResponseDTO {
    private Long id;
    private String productName;
    private Integer quantity;
    private Double price;
    private UserResponseDTO user;
}