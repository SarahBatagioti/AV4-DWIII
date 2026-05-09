package com.autobots.automanager.modelo.dto.usuario;

import java.util.Date;
import java.util.Set;

import javax.validation.Valid;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;

import lombok.Data;

@Data
public class UsuarioAtualizacaoDTO {
	private String nome;
	private String nomeSocial;
	private Date dataNascimento;
	private Set<PerfilUsuario> perfis;
	@Valid
	private EnderecoDTO endereco;
}
