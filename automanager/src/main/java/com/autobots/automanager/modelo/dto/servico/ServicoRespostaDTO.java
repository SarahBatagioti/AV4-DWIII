package com.autobots.automanager.modelo.dto.servico;

import lombok.Data;

@Data
public class ServicoRespostaDTO {
	private Long id;
	private Long empresaId;
	private String nome;
	private Double valor;
	private String descricao;
}
