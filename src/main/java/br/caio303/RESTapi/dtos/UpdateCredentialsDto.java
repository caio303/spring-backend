package br.caio303.RESTapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateCredentialsDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(max = 70)
	private String nome;
	
	@NotBlank
	@Size(min = 8)
	private String email;
	
	@NotBlank
	@Size(max = 300)
	private String descricao;
	
	@NotBlank
	private String senha;
	
	@NotBlank
	@Size(min = 10, max = 10)
	private String dataNasc;

}
