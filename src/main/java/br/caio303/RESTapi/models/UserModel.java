package br.caio303.RESTapi.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity(name = "user")
public class UserModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_user;
	
	@Column(nullable = false, length = 70)
	private String nome;

	@Column(nullable = false, unique = true, length = 11)
	private String cpf;
	
	@Column(nullable = false, length = 70)
	private String email;
	
	@Column(nullable = false, length = 300)
	private String descricao;
	
	@Column(nullable = false, length = 70)
	@JsonIgnore
	private String senha;
	
	@Column(nullable = false, length = 10)
	private String dataNasc;
	
}
