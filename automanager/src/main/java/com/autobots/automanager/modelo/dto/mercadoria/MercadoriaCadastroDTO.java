package com.autobots.automanager.modelo.dto.mercadoria;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.autobots.automanager.configuracao.DesserializadorDataFlexivel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class MercadoriaCadastroDTO {
	@NotNull(message = "validade deve ser informada")
	@JsonDeserialize(using = DesserializadorDataFlexivel.class)
	private Date validade;
	@NotNull(message = "fabricao deve ser informada")
	@JsonDeserialize(using = DesserializadorDataFlexivel.class)
	private Date fabricao;
	@NotBlank(message = "nome deve ser informado")
	private String nome;
	@NotNull(message = "quantidade deve ser informada")
	private Long quantidade;
	@NotNull(message = "valor deve ser informado")
	private Double valor;
	private String descricao;
	private Long fornecedorId;
}
