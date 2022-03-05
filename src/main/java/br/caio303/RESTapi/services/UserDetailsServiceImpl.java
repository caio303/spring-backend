package br.caio303.RESTapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.caio303.RESTapi.models.UserModel;
import br.caio303.RESTapi.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		UserModel user = userService.findByCpf(cpf);
		if(user == null) {
			throw new UsernameNotFoundException(cpf);
		}
		
		return new UserSS(user.getId(), user.getCpf(), user.getSenha());
	}

}
