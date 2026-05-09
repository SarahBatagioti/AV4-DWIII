package com.autobots.automanager.modelo.atualizadores.email;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.modelo.dto.email.EmailDTO;

@Component
public class EmailAtualizador {

	public Email criar(EmailDTO dto) {
		Email email = new Email();
		atualizar(email, dto);
		return email;
	}

	public void atualizar(Email email, EmailDTO dto) {
		email.setEndereco(dto.getEndereco());
	}
}
