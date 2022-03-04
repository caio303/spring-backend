package br.caio303.RESTapi.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.caio303.RESTapi.models.UserModel;
import br.caio303.RESTapi.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userReposity;
	
	@Transactional
	public UserModel save(UserModel userModel){
		return userReposity.save(userModel);
	}
	// TODO
	public List<UserModel> findAll() {
		return userReposity.findAll();
	}
	public UserModel findByCpf(String cpf) {
		return userReposity.findByCpf(cpf);
	}
	
	public boolean existsByCpf(String cpf) {
		if(userReposity.existsByCpf(cpf)) return true;
		return false;
	}

}
