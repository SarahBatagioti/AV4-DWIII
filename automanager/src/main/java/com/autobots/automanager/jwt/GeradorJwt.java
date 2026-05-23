package com.autobots.automanager.jwt;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

class GeradorJwt {

	private final String assinatura;
	private final Date expiracao;

	GeradorJwt(String assinatura, long duracao) {
		this.assinatura = assinatura;
		this.expiracao = new Date(System.currentTimeMillis() + duracao);
	}

	String gerarJwt(String nomeUsuario) {
		return Jwts.builder().setSubject(nomeUsuario).setExpiration(expiracao)
				.signWith(SignatureAlgorithm.HS512, assinatura.getBytes()).compact();
	}
}
