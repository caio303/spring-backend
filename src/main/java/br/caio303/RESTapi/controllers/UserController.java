package br.caio303.RESTapi.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.caio303.RESTapi.dtos.CredentialsDto;
import br.caio303.RESTapi.dtos.UserDto;
import br.caio303.RESTapi.models.UserModel;
import br.caio303.RESTapi.services.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/usuario")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping(path = "/cadastro")
	public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto userDto) throws Exception {
		if(userService.existsByCpf(userDto.getCpf())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new Exception("User alredy exists"));
		}
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
	}
	
	@PostMapping(path = "/login")
	public ResponseEntity<Object> signIn(@RequestBody @Valid CredentialsDto credentials) {
		
		// TODO Implementar essa classe. Aqui eu vou ter que verificar e gerar o token (por essa url)
		
		return null;
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UserModel>> listUsers() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
	}
	
	@GetMapping("usuario/{cpf}")
	public ResponseEntity<Object> showUser(@PathVariable String cpf) throws Exception {
		if(!userService.existsByCpf(cpf)) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Exception("User not found."));
		
		return ResponseEntity.ok(userService.findByCpf(cpf));
	}
	
}
