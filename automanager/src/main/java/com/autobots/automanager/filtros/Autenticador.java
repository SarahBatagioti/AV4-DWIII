package com.autobots.automanager.filtros;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.servicos.autenticacao.AutenticacaoUsuarioServico;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Autenticador extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager gerenciadorAutenticacao;
	private final ProvedorJwt provedorJwt;
	private final AutenticacaoUsuarioServico autenticacaoUsuarioServico;

	public Autenticador(AuthenticationManager gerenciadorAutenticacao, ProvedorJwt provedorJwt,
			AutenticacaoUsuarioServico autenticacaoUsuarioServico) {
		this.gerenciadorAutenticacao = gerenciadorAutenticacao;
		this.provedorJwt = provedorJwt;
		this.autenticacaoUsuarioServico = autenticacaoUsuarioServico;
		setFilterProcessesUrl("/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LoginRequest credencial;
		try {
			credencial = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
		} catch (IOException ex) {
			credencial = new LoginRequest();
		}
		UsernamePasswordAuthenticationToken dadosAutenticacao = new UsernamePasswordAuthenticationToken(
				credencial.getNomeUsuario(), credencial.getSenha(), new ArrayList<>());
		return gerenciadorAutenticacao.authenticate(dadosAutenticacao);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication autenticacao) throws IOException, ServletException {
		UserDetails usuario = (UserDetails) autenticacao.getPrincipal();
		String nomeUsuario = usuario.getUsername();
		String jwt = provedorJwt.proverJwt(nomeUsuario);
		autenticacaoUsuarioServico.registrarUltimoAcesso(nomeUsuario);
		response.addHeader("Authorization", "Bearer " + jwt);
	}

	public static class LoginRequest {
		private String nomeUsuario = "";
		private String senha = "";

		public String getNomeUsuario() {
			return nomeUsuario;
		}

		public void setNomeUsuario(String nomeUsuario) {
			this.nomeUsuario = nomeUsuario;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}
	}
}
