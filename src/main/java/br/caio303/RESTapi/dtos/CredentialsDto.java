package br.caio303.RESTapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CredentialsDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Size(min = 11,max = 11)
	private String cpf;
	@NotBlank
	@Size(max = 70)
	private String senha;

}
