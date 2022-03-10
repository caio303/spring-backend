package br.caio303.RESTapi.models;

import lombok.Data;

@Data
public class LoginResponseModel {

	private UserModel authenticatedUser;
	private String token;
}
