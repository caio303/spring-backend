package br.caio303.RESTapi.dtos;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDto {
	
	@NotBlank
	@Size(max = 70)
	private String nome;
	
	@NotBlank
	@Size(min = 11,max = 11)
	private String cpf;
	
	@NotBlank
	@Size(max = 70)
	private String email;

	@NotBlank
	@Size(max = 70)
	private String senha;
	
	@NotBlank
	@Size(max = 300)
	private String descricao;
	
	@NotBlank
	@Size(max = 10, min = 10)
	private String dataNasc;
	
	
}
