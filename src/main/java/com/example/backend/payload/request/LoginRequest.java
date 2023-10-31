package com.example.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//import javax.validation.constraints.NotBlank;


@Data
public class LoginRequest {
	@NotBlank(message = "Input Username")
	//@Length(min = 5, max = 50)
	private String username;

	@NotBlank(message = "Input Password")
	//@Length(min = 5, max = 10)
	private String password;

	public LoginRequest() {
	}

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
