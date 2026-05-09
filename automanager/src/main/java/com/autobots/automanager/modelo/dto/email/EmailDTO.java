package com.autobots.automanager.modelo.dto.email;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EmailDTO {
	private Long id;
	@NotBlank(message = "endereco deve ser informado")
	@Email(message = "endereco deve ser um e-mail válido")
	private String endereco;
}
