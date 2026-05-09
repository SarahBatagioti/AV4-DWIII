package com.autobots.automanager.modelo.dto.mercadoria;

import java.util.Date;

import lombok.Data;

@Data
public class MercadoriaRespostaDTO {
	private Long id;
	private Long empresaId;
	private Long fornecedorId;
	private Date validade;
	private Date fabricao;
	private Date cadastro;
	private String nome;
	private Long quantidade;
	private Double valor;
	private String descricao;
}
