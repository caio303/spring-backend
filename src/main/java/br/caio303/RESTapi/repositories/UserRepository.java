package br.caio303.RESTapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.caio303.RESTapi.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
	
	UserModel findByCpf(String cpf);
	
	boolean existsByCpf(String cpf);
	
	@Query(nativeQuery = true, value = "DELETE * FROM user"
			+ "WHERE"
			+ "cpf LIKE (:cpf)")
	void deleteByCpf(String cpf);
	
}
