package com.autobots.automanager.filtros;

class AnalisadorCabecalho {

	private final String cabecalho;

	AnalisadorCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	String obterJwt() {
		return cabecalho.replace("Bearer ", "").trim();
	}
}
