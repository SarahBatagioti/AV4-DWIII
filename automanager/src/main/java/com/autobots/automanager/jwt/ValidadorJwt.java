package com.autobots.automanager.jwt;

import java.util.Date;

import io.jsonwebtoken.Claims;

class ValidadorJwt {

	boolean validar(Claims reivindicacoes) {
		return reivindicacoes != null && reivindicacoes.getSubject() != null
				&& reivindicacoes.getExpiration() != null
				&& reivindicacoes.getExpiration().after(new Date());
	}
}
