package com.autobots.automanager.filtros;

class ValidadorCabecalho {

	private final String cabecalho;

	ValidadorCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	boolean validar() {
		return cabecalho != null && cabecalho.startsWith("Bearer ");
	}
}
