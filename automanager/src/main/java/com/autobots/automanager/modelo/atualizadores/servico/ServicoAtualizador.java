package com.autobots.automanager.modelo.atualizadores.servico;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelo.dto.servico.ServicoAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoCadastroDTO;

@Component
public class ServicoAtualizador {

	public Servico criar(ServicoCadastroDTO dto) {
		Servico servico = new Servico();
		atualizar(servico, dto.getNome(), dto.getValor(), dto.getDescricao());
		return servico;
	}

	public void atualizar(Servico servico, ServicoAtualizacaoDTO dto) {
		atualizar(servico, dto.getNome(), dto.getValor(), dto.getDescricao());
	}

	private void atualizar(Servico servico, String nome, Double valor, String descricao) {
		if (nome != null) {
			servico.setNome(nome);
		}
		if (valor != null) {
			servico.setValor(valor);
		}
		if (descricao != null) {
			servico.setDescricao(descricao);
		}
	}
}
