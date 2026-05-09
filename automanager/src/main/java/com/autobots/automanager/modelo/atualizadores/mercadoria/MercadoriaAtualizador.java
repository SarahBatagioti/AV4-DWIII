package com.autobots.automanager.modelo.atualizadores.mercadoria;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaCadastroDTO;

@Component
public class MercadoriaAtualizador {

	public Mercadoria criar(MercadoriaCadastroDTO dto) {
		Mercadoria mercadoria = new Mercadoria();
		mercadoria.setCadastro(new Date());
		atualizar(mercadoria, dto.getValidade(), dto.getFabricao(), dto.getNome(), dto.getQuantidade(), dto.getValor(),
				dto.getDescricao());
		return mercadoria;
	}

	public void atualizar(Mercadoria mercadoria, MercadoriaAtualizacaoDTO dto) {
		atualizar(mercadoria, dto.getValidade(), dto.getFabricao(), dto.getNome(), dto.getQuantidade(), dto.getValor(),
				dto.getDescricao());
	}

	private void atualizar(Mercadoria mercadoria, Date validade, Date fabricacao, String nome, Long quantidade,
			Double valor, String descricao) {
		if (validade != null) {
			mercadoria.setValidade(validade);
		}
		if (fabricacao != null) {
			mercadoria.setFabricao(fabricacao);
		}
		if (nome != null) {
			mercadoria.setNome(nome);
		}
		if (quantidade != null) {
			mercadoria.setQuantidade(quantidade);
		}
		if (valor != null) {
			mercadoria.setValor(valor);
		}
		if (descricao != null) {
			mercadoria.setDescricao(descricao);
		}
	}
}
