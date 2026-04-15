package com.draculavenom.security.auth;

import java.util.List;

import com.draculavenom.usersHandler.dto.AccountOption;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;

  private Boolean requiresSelection;
  private List<AccountOption> accounts;
}
