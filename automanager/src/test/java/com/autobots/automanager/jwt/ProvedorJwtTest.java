package com.autobots.automanager.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ProvedorJwtTest {

	@Test
	void deveGerarValidarEExtrairNomeDoUsuario() {
		ProvedorJwt provedorJwt = new ProvedorJwt();
		ReflectionTestUtils.setField(provedorJwt, "assinatura", "segredo-de-teste");
		ReflectionTestUtils.setField(provedorJwt, "duracao", 60000L);

		String jwt = provedorJwt.proverJwt("usuario.teste");

		assertTrue(provedorJwt.validarJwt(jwt));
		assertEquals("usuario.teste", provedorJwt.obterNomeUsuario(jwt));
	}

	@Test
	void deveInvalidarTokenExpirado() throws InterruptedException {
		ProvedorJwt provedorJwt = new ProvedorJwt();
		ReflectionTestUtils.setField(provedorJwt, "assinatura", "segredo-expiracao");
		ReflectionTestUtils.setField(provedorJwt, "duracao", 1L);

		String jwt = provedorJwt.proverJwt("usuario.expirado");
		Thread.sleep(10L);

		assertFalse(provedorJwt.validarJwt(jwt));
	}
}
