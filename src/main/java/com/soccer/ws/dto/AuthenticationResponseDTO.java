package com.soccer.ws.dto;

public class AuthenticationResponseDTO {

	private static final long serialVersionUID = -6624726180748515507L;
	private String token;

	public AuthenticationResponseDTO() {
		super();
	}

	public AuthenticationResponseDTO(String token) {
		this.setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
