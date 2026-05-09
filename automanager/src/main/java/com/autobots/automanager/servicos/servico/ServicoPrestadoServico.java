package com.autobots.automanager.servicos.servico;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelo.atualizadores.servico.ServicoAtualizador;
import com.autobots.automanager.modelo.dto.servico.ServicoAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoCadastroDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoRespostaDTO;
import com.autobots.automanager.modelo.mapeadores.RespostaMapper;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.servicos.SuporteDominioServico;

@Service
public class ServicoPrestadoServico {

	@Autowired
	private ServicoRepositorio servicoRepositorio;

	@Autowired
	private ServicoAtualizador servicoAtualizador;

	@Autowired
	private RespostaMapper respostaMapper;

	@Autowired
	private SuporteDominioServico suporte;

	public List<ServicoRespostaDTO> listarDaEmpresa(Long empresaId) {
		return suporte.buscarEmpresa(empresaId).getServicos().stream().map(respostaMapper::paraServico)
				.collect(Collectors.toList());
	}

	public ServicoRespostaDTO buscarDaEmpresa(Long empresaId, Long servicoId) {
		return respostaMapper.paraServico(suporte.buscarServicoDaEmpresa(empresaId, servicoId));
	}

	public ServicoRespostaDTO cadastrar(Long empresaId, ServicoCadastroDTO dto) {
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		Servico servico = servicoAtualizador.criar(dto);
		servico.setEmpresa(empresa);
		return respostaMapper.paraServico(servicoRepositorio.save(servico));
	}

	public ServicoRespostaDTO atualizar(Long empresaId, Long servicoId, ServicoAtualizacaoDTO dto) {
		Servico servico = suporte.buscarServicoDaEmpresa(empresaId, servicoId);
		servicoAtualizador.atualizar(servico, dto);
		return respostaMapper.paraServico(servicoRepositorio.save(servico));
	}

	public void remover(Long empresaId, Long servicoId) {
		servicoRepositorio.delete(suporte.buscarServicoDaEmpresa(empresaId, servicoId));
	}
}
