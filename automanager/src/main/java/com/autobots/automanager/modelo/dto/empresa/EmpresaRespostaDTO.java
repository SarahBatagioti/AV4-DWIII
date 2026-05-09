package com.autobots.automanager.modelo.dto.empresa;

import java.util.Date;
import java.util.List;

import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;

import lombok.Data;

@Data
public class EmpresaRespostaDTO {
	private Long id;
	private String razaoSocial;
	private String nomeFantasia;
	private Date cadastro;
	private EnderecoDTO endereco;
	private List<TelefoneDTO> telefones;
	private List<Long> usuariosIds;
	private List<Long> mercadoriasIds;
	private List<Long> servicosIds;
	private List<Long> vendasIds;
}
