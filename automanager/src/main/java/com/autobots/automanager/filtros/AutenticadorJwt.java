package com.autobots.automanager.filtros;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.autobots.automanager.jwt.ProvedorJwt;

class AutenticadorJwt {

	private final String jwt;
	private final ProvedorJwt provedorJwt;
	private final UserDetailsService servico;

	AutenticadorJwt(String jwt, ProvedorJwt provedorJwt, UserDetailsService servico) {
		this.jwt = jwt;
		this.provedorJwt = provedorJwt;
		this.servico = servico;
	}

	UsernamePasswordAuthenticationToken obterAutenticacao() {
		if (!provedorJwt.validarJwt(jwt)) {
			return null;
		}
		String nomeUsuario = provedorJwt.obterNomeUsuario(jwt);
		UserDetails usuario = servico.loadUserByUsername(nomeUsuario);
		return new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), usuario.getAuthorities());
	}
}
