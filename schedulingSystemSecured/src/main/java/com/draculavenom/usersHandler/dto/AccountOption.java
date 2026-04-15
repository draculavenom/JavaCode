package com.draculavenom.usersHandler.dto;

import com.draculavenom.security.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOption {
    private Integer userId;
    private Role role;
    private Integer managedBy;

}
