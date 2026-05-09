package com.autobots.automanager.modelo.dto.venda;

import java.util.Set;

import lombok.Data;

@Data
public class VendaAtualizacaoDTO {
	private String identificacao;
	private Long clienteId;
	private Long funcionarioId;
	private Long veiculoId;
	private Set<Long> mercadoriasIds;
	private Set<Long> servicosIds;
}
