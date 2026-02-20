package com.draculavenom.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.draculavenom.security.user.Permission.ADMIN_CREATE;
import static com.draculavenom.security.user.Permission.ADMIN_DELETE;
import static com.draculavenom.security.user.Permission.ADMIN_READ;
import static com.draculavenom.security.user.Permission.ADMIN_UPDATE;
import static com.draculavenom.security.user.Permission.MANAGER_CREATE;
import static com.draculavenom.security.user.Permission.MANAGER_DELETE;
import static com.draculavenom.security.user.Permission.MANAGER_READ;
import static com.draculavenom.security.user.Permission.MANAGER_UPDATE;
import static com.draculavenom.security.user.Permission.USER_CREATE;
import static com.draculavenom.security.user.Permission.USER_DELETE;
import static com.draculavenom.security.user.Permission.USER_READ;
import static com.draculavenom.security.user.Permission.USER_UPDATE;

@RequiredArgsConstructor
public enum Role {

  USER(
		  Set.of(
                  USER_READ,
                  USER_UPDATE,
                  USER_DELETE,
                  USER_CREATE
          )
  ),
  ADMIN(
          Set.of(
                  ADMIN_READ,
                  ADMIN_UPDATE,
                  ADMIN_DELETE,
                  ADMIN_CREATE,
                  MANAGER_READ,
                  MANAGER_UPDATE,
                  MANAGER_DELETE,
                  MANAGER_CREATE,
                  USER_READ,
                  USER_UPDATE,
                  USER_DELETE,
                  USER_CREATE
          )
  ),
  MANAGER(
          Set.of(
                  MANAGER_READ,
                  MANAGER_UPDATE,
                  MANAGER_DELETE,
                  MANAGER_CREATE,
                  USER_READ,
                  USER_UPDATE,
                  USER_DELETE,
                  USER_CREATE
          )
  )

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
