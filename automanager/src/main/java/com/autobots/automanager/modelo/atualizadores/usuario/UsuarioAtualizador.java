package com.autobots.automanager.modelo.atualizadores.usuario;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.dto.usuario.UsuarioAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioCadastroDTO;

@Component
public class UsuarioAtualizador {

	public Usuario criar(UsuarioCadastroDTO dto) {
		Usuario usuario = new Usuario();
		usuario.setDataCadastro(new Date());
		usuario.setPerfis(dto.getPerfis());
		atualizar(usuario, dto.getNome(), dto.getNomeSocial(), dto.getDataNascimento());
		return usuario;
	}

	public void atualizar(Usuario usuario, UsuarioAtualizacaoDTO dto) {
		atualizar(usuario, dto.getNome(), dto.getNomeSocial(), dto.getDataNascimento());
		if (dto.getPerfis() != null && !dto.getPerfis().isEmpty()) {
			usuario.getPerfis().clear();
			usuario.getPerfis().addAll(dto.getPerfis());
		}
	}

	private void atualizar(Usuario usuario, String nome, String nomeSocial, Date dataNascimento) {
		if (nome != null) {
			usuario.setNome(nome);
		}
		if (nomeSocial != null) {
			usuario.setNomeSocial(nomeSocial);
		}
		if (dataNascimento != null) {
			usuario.setDataNascimento(dataNascimento);
		}
	}
}
