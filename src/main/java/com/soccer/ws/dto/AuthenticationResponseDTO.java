package com.soccer.ws.dto;

import java.util.List;

public class AuthenticationResponseDTO extends BaseClassDTO {

	private static final long serialVersionUID = -6624726180748515507L;
	private String token;
	private String firstName;
	private String lastName;
	private List<String> roles;

	public AuthenticationResponseDTO() {
		super();
	}

	public AuthenticationResponseDTO(String token, long id, String firstName, String lastName, List<String> roles) {
		super(id);
		this.setToken(token);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setRoles(roles);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
