package com.autobots.automanager.modelo.dto.endereco;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EnderecoDTO {
	private Long id;
	@NotBlank(message = "estado deve ser informado")
	private String estado;
	@NotBlank(message = "cidade deve ser informada")
	private String cidade;
	@NotBlank(message = "bairro deve ser informado")
	private String bairro;
	@NotBlank(message = "rua deve ser informada")
	private String rua;
	@NotBlank(message = "numero deve ser informado")
	private String numero;
	@NotBlank(message = "codigoPostal deve ser informado")
	private String codigoPostal;
	private String informacoesAdicionais;
}
