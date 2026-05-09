package com.autobots.automanager.modelo.dto.empresa;

import javax.validation.Valid;

import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;

import lombok.Data;

@Data
public class EmpresaAtualizacaoDTO {
	private String razaoSocial;
	private String nomeFantasia;
	@Valid
	private EnderecoDTO endereco;
}
