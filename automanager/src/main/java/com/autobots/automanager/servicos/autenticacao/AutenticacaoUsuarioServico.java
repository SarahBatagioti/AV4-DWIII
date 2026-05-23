package com.autobots.automanager.servicos.autenticacao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.CredencialUsuarioSenhaRepositorio;

@Service
public class AutenticacaoUsuarioServico {

	@Autowired
	private CredencialUsuarioSenhaRepositorio credencialUsuarioSenhaRepositorio;

	public CredencialUsuarioSenha buscarCredencialAtivaPorNomeUsuario(String nomeUsuario) {
		return credencialUsuarioSenhaRepositorio.findByNomeUsuario(nomeUsuario)
				.filter(credencial -> !credencial.isInativo())
				.filter(credencial -> credencial.getUsuario() != null)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}

	public Usuario buscarUsuarioAtivoPorNomeUsuario(String nomeUsuario) {
		return buscarCredencialAtivaPorNomeUsuario(nomeUsuario).getUsuario();
	}

	@Transactional
	public void registrarUltimoAcesso(String nomeUsuario) {
		CredencialUsuarioSenha credencial = buscarCredencialAtivaPorNomeUsuario(nomeUsuario);
		credencial.setUltimoAcesso(new Date());
		credencialUsuarioSenhaRepositorio.save(credencial);
	}
}
