package com.autobots.automanager.modelo.dto.servico;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ServicoCadastroDTO {
	@NotBlank(message = "nome deve ser informado")
	private String nome;
	@NotNull(message = "valor deve ser informado")
	private Double valor;
	private String descricao;
}
