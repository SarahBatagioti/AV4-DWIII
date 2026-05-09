package com.autobots.automanager.modelo.atualizadores.empresa;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelo.dto.empresa.EmpresaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaCadastroDTO;

@Component
public class EmpresaAtualizador {

	public Empresa criar(EmpresaCadastroDTO dto) {
		Empresa empresa = new Empresa();
		empresa.setCadastro(new Date());
		atualizar(empresa, dto.getRazaoSocial(), dto.getNomeFantasia());
		return empresa;
	}

	public void atualizar(Empresa empresa, EmpresaAtualizacaoDTO dto) {
		atualizar(empresa, dto.getRazaoSocial(), dto.getNomeFantasia());
	}

	private void atualizar(Empresa empresa, String razaoSocial, String nomeFantasia) {
		if (razaoSocial != null) {
			empresa.setRazaoSocial(razaoSocial);
		}
		if (nomeFantasia != null) {
			empresa.setNomeFantasia(nomeFantasia);
		}
	}
}
