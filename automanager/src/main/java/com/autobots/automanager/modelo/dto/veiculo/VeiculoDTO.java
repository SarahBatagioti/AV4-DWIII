package com.autobots.automanager.modelo.dto.veiculo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.autobots.automanager.enumeracoes.TipoVeiculo;

import lombok.Data;

@Data
public class VeiculoDTO {
	private Long id;
	@NotNull(message = "tipo deve ser informado")
	private TipoVeiculo tipo;
	@NotBlank(message = "modelo deve ser informado")
	private String modelo;
	@NotBlank(message = "placa deve ser informada")
	private String placa;
}
