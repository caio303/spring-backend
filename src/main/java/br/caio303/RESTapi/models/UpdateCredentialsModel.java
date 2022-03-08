package br.caio303.RESTapi.models;

import lombok.Data;

@Data
public class UpdateCredentialsModel {
	
	private String nome;
	private String email;
	private String descricao;
	private String senha;
	private String dataNasc;
	
}
