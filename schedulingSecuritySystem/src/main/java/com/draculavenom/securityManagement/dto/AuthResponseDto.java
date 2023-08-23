package com.draculavenom.securityManagement.dto;

public class AuthResponseDto {
	private String accessToken;
	private String tokenType;
	public AuthResponseDto(String accessToken) {
		this.accessToken = accessToken;
		this.tokenType = "bearer";
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
