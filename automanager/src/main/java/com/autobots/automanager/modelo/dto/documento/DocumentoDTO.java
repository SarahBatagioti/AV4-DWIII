package com.autobots.automanager.modelo.dto.documento;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.autobots.automanager.enumeracoes.TipoDocumento;

import lombok.Data;

@Data
public class DocumentoDTO {
	private Long id;
	@NotNull(message = "tipo deve ser informado")
	private TipoDocumento tipo;
	@NotNull(message = "dataEmissao deve ser informada")
	private Date dataEmissao;
	@NotBlank(message = "numero deve ser informado")
	private String numero;
}
