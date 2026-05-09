package com.autobots.automanager.modelo.atualizadores.telefone;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;

@Component
public class TelefoneAtualizador {

	public Telefone criar(TelefoneDTO dto) {
		Telefone telefone = new Telefone();
		atualizar(telefone, dto);
		return telefone;
	}

	public void atualizar(Telefone telefone, TelefoneDTO dto) {
		telefone.setDdd(dto.getDdd());
		telefone.setNumero(dto.getNumero());
	}
}
