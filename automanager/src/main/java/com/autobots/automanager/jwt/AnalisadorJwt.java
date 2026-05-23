package com.autobots.automanager.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

class AnalisadorJwt {

	private final String assinatura;
	private final String jwt;

	AnalisadorJwt(String assinatura, String jwt) {
		this.assinatura = assinatura;
		this.jwt = jwt;
	}

	Claims obterReivindicacoes() {
		return Jwts.parser().setSigningKey(assinatura.getBytes()).parseClaimsJws(jwt).getBody();
	}

	String obterNomeUsuario(Claims reivindicacoes) {
		return reivindicacoes.getSubject();
	}
}
