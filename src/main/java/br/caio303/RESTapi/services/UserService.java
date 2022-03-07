package br.caio303.RESTapi.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.caio303.RESTapi.models.UserModel;
import br.caio303.RESTapi.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userReposity;
	
	@Transactional
	public UserModel save(UserModel userModel) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] senhaEmBytes = userModel.getSenha().getBytes(StandardCharsets.UTF_8);
		byte[] senhaEncriptada = md.digest(senhaEmBytes);
		userModel.setSenha(new String(senhaEncriptada, StandardCharsets.UTF_8));
		
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
