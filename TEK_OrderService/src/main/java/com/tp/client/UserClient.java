package com.tp.client;


import com.tp.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// "name" must match the spring.application.name registered in Eureka for User Service
@FeignClient(name = "user-service")
public interface UserClient {

    // Matches the exact endpoint exposed by TEK_UserService's UserController
    @GetMapping("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable("id") Long id);
}
