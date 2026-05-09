package com.autobots.automanager.modelo.dto.mercadoria;

import java.util.Date;

import lombok.Data;

@Data
public class MercadoriaAtualizacaoDTO {
	private Date validade;
	private Date fabricao;
	private String nome;
	private Long quantidade;
	private Double valor;
	private String descricao;
	private Long fornecedorId;
}
