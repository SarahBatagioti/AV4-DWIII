package com.autobots.automanager.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class ProvedorJwt {

	@Value("${jwt.secret}")
	private String assinatura;

	@Value("${jwt.expiration}")
	private Long duracao;

	public String proverJwt(String nomeUsuario) {
		return new GeradorJwt(assinatura, duracao).gerarJwt(nomeUsuario);
	}

	public boolean validarJwt(String jwt) {
		try {
			Claims reivindicacoes = new AnalisadorJwt(assinatura, jwt).obterReivindicacoes();
			return new ValidadorJwt().validar(reivindicacoes);
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	public String obterNomeUsuario(String jwt) {
		Claims reivindicacoes = new AnalisadorJwt(assinatura, jwt).obterReivindicacoes();
		return new AnalisadorJwt(assinatura, jwt).obterNomeUsuario(reivindicacoes);
	}
}
