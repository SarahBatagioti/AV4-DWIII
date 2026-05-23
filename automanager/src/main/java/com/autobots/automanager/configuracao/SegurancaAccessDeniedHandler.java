package com.autobots.automanager.configuracao;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.excecoes.ApiErroResposta;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SegurancaAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getOutputStream(),
				new ApiErroResposta(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
						"Acesso negado", request.getRequestURI()));
	}
}
