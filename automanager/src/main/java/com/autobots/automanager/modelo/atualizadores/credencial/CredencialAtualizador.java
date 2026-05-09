package com.autobots.automanager.modelo.atualizadores.credencial;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.excecoes.RequisicaoInvalidaException;
import com.autobots.automanager.modelo.dto.credencial.CredencialDTO;

@Component
public class CredencialAtualizador {

	public Credencial criar(CredencialDTO dto) {
		Credencial credencial = novaInstancia(dto.getTipo());
		atualizar(credencial, dto);
		if (credencial.getCriacao() == null) {
			credencial.setCriacao(new Date());
		}
		return credencial;
	}

	public void atualizar(Credencial credencial, CredencialDTO dto) {
		if (dto.getCriacao() != null) {
			credencial.setCriacao(dto.getCriacao());
		}
		if (dto.getUltimoAcesso() != null) {
			credencial.setUltimoAcesso(dto.getUltimoAcesso());
		}
		credencial.setInativo(Boolean.TRUE.equals(dto.getInativo()));

		if (credencial instanceof CredencialUsuarioSenha) {
			CredencialUsuarioSenha atual = (CredencialUsuarioSenha) credencial;
			if (dto.getNomeUsuario() != null) {
				atual.setNomeUsuario(dto.getNomeUsuario());
			}
			if (dto.getSenha() != null) {
				atual.setSenha(dto.getSenha());
			}
			return;
		}

		if (credencial instanceof CredencialCodigoBarra) {
			CredencialCodigoBarra atual = (CredencialCodigoBarra) credencial;
			if (dto.getCodigo() != null) {
				atual.setCodigo(dto.getCodigo());
			}
			return;
		}

		throw new RequisicaoInvalidaException("Tipo de credencial não suportado");
	}

	public boolean mesmoTipo(Credencial credencial, String tipo) {
		if (credencial instanceof CredencialUsuarioSenha) {
			return "usuario-senha".equalsIgnoreCase(tipo);
		}
		if (credencial instanceof CredencialCodigoBarra) {
			return "codigo-barra".equalsIgnoreCase(tipo);
		}
		return false;
	}

	private Credencial novaInstancia(String tipo) {
		if ("usuario-senha".equalsIgnoreCase(tipo)) {
			return new CredencialUsuarioSenha();
		}
		if ("codigo-barra".equalsIgnoreCase(tipo)) {
			return new CredencialCodigoBarra();
		}
		throw new RequisicaoInvalidaException("tipo deve ser 'usuario-senha' ou 'codigo-barra'");
	}
}
