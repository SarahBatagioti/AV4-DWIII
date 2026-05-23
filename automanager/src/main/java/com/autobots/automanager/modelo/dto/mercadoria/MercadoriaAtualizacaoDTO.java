package com.autobots.automanager.modelo.dto.mercadoria;

import java.util.Date;

import com.autobots.automanager.configuracao.DesserializadorDataFlexivel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class MercadoriaAtualizacaoDTO {
	@JsonDeserialize(using = DesserializadorDataFlexivel.class)
	private Date validade;
	@JsonDeserialize(using = DesserializadorDataFlexivel.class)
	private Date fabricao;
	private String nome;
	private Long quantidade;
	private Double valor;
	private String descricao;
	private Long fornecedorId;
}
