package com.draculavenom.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Long managedBy;
    private String role;
}
