package com.autobots.automanager.modelo.atualizadores.endereco;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;

@Component
public class EnderecoAtualizador {

	public Endereco criar(EnderecoDTO dto) {
		Endereco endereco = new Endereco();
		atualizar(endereco, dto);
		return endereco;
	}

	public void atualizar(Endereco endereco, EnderecoDTO dto) {
		endereco.setEstado(dto.getEstado());
		endereco.setCidade(dto.getCidade());
		endereco.setBairro(dto.getBairro());
		endereco.setRua(dto.getRua());
		endereco.setNumero(dto.getNumero());
		endereco.setCodigoPostal(dto.getCodigoPostal());
		endereco.setInformacoesAdicionais(dto.getInformacoesAdicionais());
	}
}
