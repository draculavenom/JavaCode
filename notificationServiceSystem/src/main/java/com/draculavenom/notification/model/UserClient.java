package com.draculavenom.notification.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userServiceSystem")
public interface UserClient {
    
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable Integer id);
}
