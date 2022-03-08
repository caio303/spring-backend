package br.caio303.RESTapi.models;

import lombok.Data;

@Data
public class LoginCredentialsModel {
	
	private String cpf;
	private String senha;
	
}
