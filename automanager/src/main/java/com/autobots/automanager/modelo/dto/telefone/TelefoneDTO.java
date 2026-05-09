package com.autobots.automanager.modelo.dto.telefone;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TelefoneDTO {
	private Long id;
	@NotBlank(message = "ddd deve ser informado")
	private String ddd;
	@NotBlank(message = "numero deve ser informado")
	private String numero;
}
