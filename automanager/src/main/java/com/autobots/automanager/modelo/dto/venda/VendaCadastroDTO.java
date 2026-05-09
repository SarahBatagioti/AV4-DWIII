package com.autobots.automanager.modelo.dto.venda;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VendaCadastroDTO {
	@NotBlank(message = "identificacao deve ser informada")
	private String identificacao;
	@NotNull(message = "clienteId deve ser informado")
	private Long clienteId;
	@NotNull(message = "funcionarioId deve ser informado")
	private Long funcionarioId;
	private Long veiculoId;
	private Set<Long> mercadoriasIds;
	private Set<Long> servicosIds;
}
