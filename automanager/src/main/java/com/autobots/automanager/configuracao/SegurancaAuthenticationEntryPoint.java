package com.autobots.automanager.configuracao;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.excecoes.ApiErroResposta;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SegurancaAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getOutputStream(),
				new ApiErroResposta(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
						"Autenticação obrigatória", request.getRequestURI()));
	}
}
