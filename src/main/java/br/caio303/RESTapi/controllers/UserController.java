package br.caio303.RESTapi.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.caio303.RESTapi.dtos.CredentialsDto;
import br.caio303.RESTapi.dtos.UserDto;
import br.caio303.RESTapi.models.CredentialsModel;
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
		if (userService.existsByCpf(userDto.getCpf())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new Exception("User alredy exists"));
		}
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
	}

	@PostMapping(path = "/login")
	public ResponseEntity<Object> signIn(@RequestBody @Valid CredentialsDto credentialsDto)
			throws NoSuchAlgorithmException {
		if (!userService.existsByCpf(credentialsDto.getCpf())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Exception("Invalid Credentials."));
		}

		UserModel user = userService.findByCpf(credentialsDto.getCpf());

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] senhaRecebidaEmBytes = credentialsDto.getSenha().getBytes(StandardCharsets.UTF_8);
		byte[] senhaRecebidaEmBytesEncriptada = md.digest(senhaRecebidaEmBytes);
		String senhaRecebidaEncriptada = new String(senhaRecebidaEmBytesEncriptada, StandardCharsets.UTF_8);
		
		if(user.getSenha().equals(senhaRecebidaEncriptada)) { return
				ResponseEntity.status(HttpStatus.OK).body(user); 
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Credentials");
	}

	@GetMapping("/")
	public ResponseEntity<List<UserModel>> listUsers(@RequestHeader("Authentication") String authHeader) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
	}

	@GetMapping("usuario/{cpf}")
	public ResponseEntity<Object> showUser(@PathVariable String cpf, @RequestHeader("Authentication") String authHeader)
			throws Exception {
		if (authHeader == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new IOException("Not authorized request."));
		if (!userService.existsByCpf(cpf))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new IOException("User not found."));
		return ResponseEntity.ok(userService.findByCpf(cpf));
	}

}
