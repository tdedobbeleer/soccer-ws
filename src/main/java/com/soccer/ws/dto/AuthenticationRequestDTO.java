package com.soccer.ws.dto;

public class AuthenticationRequestDTO {

	private static final long serialVersionUID = 6624726180748515507L;
	private String username;
	private String password;
    private boolean rememberMe;

	public AuthenticationRequestDTO() {
		super();
	}

    public AuthenticationRequestDTO(String username, String password, boolean rememberMe) {
        this.rememberMe = rememberMe;
        this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
