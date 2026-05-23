package com.autobots.automanager.configuracao;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DesserializadorDataFlexivel extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		JsonToken token = parser.currentToken();
		if (token == JsonToken.VALUE_NUMBER_INT) {
			return new Date(parser.getLongValue());
		}
		if (token == JsonToken.VALUE_STRING) {
			String texto = parser.getText();
			if (texto == null || texto.isBlank()) {
				return null;
			}
			return converterTexto(texto.trim(), context);
		}
		return (Date) context.handleUnexpectedToken(Date.class, parser);
	}

	private Date converterTexto(String texto, DeserializationContext context) throws IOException {
		try {
			return Date.from(Instant.parse(texto));
		} catch (DateTimeParseException ignored) {
		}
		try {
			return Date.from(OffsetDateTime.parse(texto).toInstant());
		} catch (DateTimeParseException ignored) {
		}
		try {
			return Date.from(LocalDate.parse(texto).atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException ignored) {
		}
		try {
			return context.parseDate(texto);
		} catch (IllegalArgumentException exception) {
			throw context.weirdStringException(texto, Date.class,
					"data inválida; use epoch em milissegundos, yyyy-MM-dd ou ISO-8601");
		}
	}
}
