package com.autobots.automanager.modelo.dto.empresa;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;

import lombok.Data;

@Data
public class EmpresaCadastroDTO {
	@NotBlank(message = "razaoSocial deve ser informada")
	private String razaoSocial;
	private String nomeFantasia;
	@Valid
	private EnderecoDTO endereco;
	@Valid
	private List<TelefoneDTO> telefones;
}
