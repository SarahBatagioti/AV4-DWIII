package com.autobots.automanager.modelo.atualizadores.documento;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.dto.documento.DocumentoDTO;

@Component
public class DocumentoAtualizador {

	public Documento criar(DocumentoDTO dto) {
		Documento documento = new Documento();
		atualizar(documento, dto);
		return documento;
	}

	public void atualizar(Documento documento, DocumentoDTO dto) {
		documento.setTipo(dto.getTipo());
		documento.setDataEmissao(dto.getDataEmissao());
		documento.setNumero(dto.getNumero());
	}
}
