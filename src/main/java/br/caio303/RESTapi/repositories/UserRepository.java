package br.caio303.RESTapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.caio303.RESTapi.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
	
	UserModel findByCpf(String cpf);
	
	boolean existsByCpf(String cpf);
	
}
