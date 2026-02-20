package com.draculavenom.security.user;

import java.time.LocalDate;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    
  @Id
  @GeneratedValue
  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  
  @Column(nullable = true)
  private String phoneNumber;
  @Column(nullable = true)
  private LocalDate dateOfBirth;
  private Integer managedBy;
  private Boolean passwordChange;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  @ToString.Exclude
  private List<Token> tokens;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private CompanyName companyName;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }
  
  public String getName() {
	  return firstName + " " + lastName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
