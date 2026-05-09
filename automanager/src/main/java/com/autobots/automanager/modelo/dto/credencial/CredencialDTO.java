package com.autobots.automanager.modelo.dto.credencial;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CredencialDTO {
	private Long id;
	@NotBlank(message = "tipo deve ser informado")
	private String tipo;
	private Date criacao;
	private Date ultimoAcesso;
	private Boolean inativo;
	private String nomeUsuario;
	private String senha;
	private Long codigo;
}
