package com.autobots.automanager.adaptadores;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.entidades.Usuario;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {

	private final Usuario usuario;

	public UserDetailsImpl(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getUsuarioId() {
		return usuario.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return usuario.getPerfis().stream().map(perfil -> new SimpleGrantedAuthority(perfil.authority()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return usuario.getCredenciais().stream()
				.filter(com.autobots.automanager.entidades.CredencialUsuarioSenha.class::isInstance)
				.map(com.autobots.automanager.entidades.CredencialUsuarioSenha.class::cast)
				.findFirst()
				.map(com.autobots.automanager.entidades.CredencialUsuarioSenha::getSenha)
				.orElse("");
	}

	@Override
	public String getUsername() {
		return usuario.getCredenciais().stream()
				.filter(com.autobots.automanager.entidades.CredencialUsuarioSenha.class::isInstance)
				.map(com.autobots.automanager.entidades.CredencialUsuarioSenha.class::cast)
				.findFirst()
				.map(com.autobots.automanager.entidades.CredencialUsuarioSenha::getNomeUsuario)
				.orElse("");
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
