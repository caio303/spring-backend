package br.caio303.RESTapi.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.caio303.RESTapi.dtos.LoginCredentialsDto;
import br.caio303.RESTapi.dtos.UpdateCredentialsDto;
import br.caio303.RESTapi.dtos.UserDto;
import br.caio303.RESTapi.models.LoginResponseModel;
import br.caio303.RESTapi.models.UpdateCredentialsModel;
import br.caio303.RESTapi.models.UserModel;
import br.caio303.RESTapi.security.JWTUtil;
import br.caio303.RESTapi.services.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/usuario")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTUtil jwtUtil;

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
	public ResponseEntity<Object> signIn(@RequestBody @Valid LoginCredentialsDto credentialsDto)
			throws NoSuchAlgorithmException {
		if (!userService.existsByCpf(credentialsDto.getCpf())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Exception("Invalid Credentials."));
		}

		UserModel user = userService.findByCpf(credentialsDto.getCpf());

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] senhaRecebidaEmBytes = credentialsDto.getSenha().getBytes(StandardCharsets.UTF_8);
		byte[] senhaRecebidaEmBytesEncriptada = md.digest(senhaRecebidaEmBytes);
		String senhaRecebidaEncriptada = new String(senhaRecebidaEmBytesEncriptada, StandardCharsets.UTF_8);

		if (!user.getSenha().equals(senhaRecebidaEncriptada)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Credentials");
		}
		
		LoginResponseModel responseModel = new LoginResponseModel();
		
		responseModel.setAuthenticatedUser(user);
		responseModel.setToken(new String("Bearer " + jwtUtil.generateToken(user.getCpf())));
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(responseModel);
	}

	@GetMapping("/{cpf}")
	public ResponseEntity<Object> showUser(@PathVariable String cpf, @RequestHeader("Authentication") String authHeader)
			throws Exception {
		if (!userService.existsByCpf(cpf))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new IOException("User not found."));

		if (authHeader == null || !jwtUtil.isTokenValid(authHeader.substring(7)))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new IOException("Unauthorized request."));

		String token = authHeader.substring(7);
		UserModel user = userService.findByCpf(cpf);
		
		if(!jwtUtil.getSubject(token).equals(user.getCpf()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new IOException("Unauthorized request."));
			
		return ResponseEntity.ok(user);
	}

	@DeleteMapping("/{cpf}")
	public ResponseEntity<Object> deleteUser(@PathVariable String cpf, @RequestHeader("Authentication") String authHeader)
			throws Exception {
		
		if (!userService.existsByCpf(cpf))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new IOException("User not found."));

		JWTUtil jwtUtil = new JWTUtil();
		if (authHeader == null || !jwtUtil.isTokenValid(authHeader.substring(7)))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new IOException("Unauthorized request."));

		String token = authHeader.substring(7);
		UserModel user = userService.findByCpf(cpf);
		
		if(!jwtUtil.getSubject(token).equals(user.getCpf()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new IOException("Unauthorized request."));
		
		userService.deleteByCpf(user.getCpf());
		
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@PutMapping("/{cpf}")
	public ResponseEntity<Object> updateUser(
				@PathVariable String cpf, 
				@RequestHeader("Authentication") String authHeader,
				@RequestBody @Valid UpdateCredentialsDto updateCredentialsDto
			)
			throws Exception {
		
		if(!userService.existsByCpf(cpf))
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new IOException("User not found."));
		

		JWTUtil jwtUtil = new JWTUtil();
		if (authHeader == null || !jwtUtil.isTokenValid(authHeader.substring(7)))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new IOException("Unauthorized request."));

		String token = authHeader.substring(7);
		UserModel user = userService.findByCpf(cpf);
		
		if(!jwtUtil.getSubject(token).equals(user.getCpf()))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new IOException("Unauthorized request."));
		
		var updatedUser = new UserModel();
		BeanUtils.copyProperties(user, updatedUser);
		
		var updateCredentialsModel = new UpdateCredentialsModel();
		BeanUtils.copyProperties(updateCredentialsDto, updateCredentialsModel);
		
		updatedUser.setNome(updateCredentialsModel.getNome());
		updatedUser.setEmail(updateCredentialsModel.getEmail());
		updatedUser.setDescricao(updateCredentialsModel.getDescricao());
		updatedUser.setSenha(updateCredentialsModel.getSenha());
		updatedUser.setDataNasc(updateCredentialsModel.getDataNasc());
		
		return ResponseEntity.status(HttpStatus.OK).body(userService.save(updatedUser));
		
	}
}
