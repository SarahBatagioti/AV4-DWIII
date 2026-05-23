package com.autobots.automanager.adaptadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.servicos.autenticacao.AutenticacaoUsuarioServico;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AutenticacaoUsuarioServico autenticacaoUsuarioServico;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = autenticacaoUsuarioServico.buscarUsuarioAtivoPorNomeUsuario(username);
		return new UserDetailsImpl(usuario);
	}
}
