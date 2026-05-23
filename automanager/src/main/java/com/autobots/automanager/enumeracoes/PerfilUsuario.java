package com.autobots.automanager.enumeracoes;

public enum PerfilUsuario {
	ADMINISTRADOR,
	GERENTE,
	VENDEDOR,
	CLIENTE;

	public String authority() {
		return "ROLE_" + name();
	}
}
