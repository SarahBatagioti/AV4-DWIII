package com.autobots.automanager.servicos.mercadoria;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.atualizadores.mercadoria.MercadoriaAtualizador;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaCadastroDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaRespostaDTO;
import com.autobots.automanager.modelo.mapeadores.RespostaMapper;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.servicos.SuporteDominioServico;

@Service
public class MercadoriaServico {

	@Autowired
	private MercadoriaRepositorio mercadoriaRepositorio;

	@Autowired
	private MercadoriaAtualizador mercadoriaAtualizador;

	@Autowired
	private RespostaMapper respostaMapper;

	@Autowired
	private SuporteDominioServico suporte;

	public List<MercadoriaRespostaDTO> listarDaEmpresa(Long empresaId) {
		return suporte.buscarEmpresa(empresaId).getMercadorias().stream().map(respostaMapper::paraMercadoria)
				.collect(Collectors.toList());
	}

	public MercadoriaRespostaDTO buscarDaEmpresa(Long empresaId, Long mercadoriaId) {
		return respostaMapper.paraMercadoria(suporte.buscarMercadoriaDaEmpresa(empresaId, mercadoriaId));
	}

	public MercadoriaRespostaDTO cadastrar(Long empresaId, MercadoriaCadastroDTO dto) {
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		Mercadoria mercadoria = mercadoriaAtualizador.criar(dto);
		mercadoria.setEmpresa(empresa);
		suporte.fornecedorDaEmpresa(empresaId, dto.getFornecedorId()).ifPresent(mercadoria::setFornecedor);
		return respostaMapper.paraMercadoria(mercadoriaRepositorio.save(mercadoria));
	}

	public MercadoriaRespostaDTO atualizar(Long empresaId, Long mercadoriaId, MercadoriaAtualizacaoDTO dto) {
		Mercadoria mercadoria = suporte.buscarMercadoriaDaEmpresa(empresaId, mercadoriaId);
		mercadoriaAtualizador.atualizar(mercadoria, dto);
		if (dto.getFornecedorId() != null) {
			mercadoria.setFornecedor(suporte.fornecedorDaEmpresa(empresaId, dto.getFornecedorId()).orElse(null));
		}
		return respostaMapper.paraMercadoria(mercadoriaRepositorio.save(mercadoria));
	}

	public void remover(Long empresaId, Long mercadoriaId) {
		mercadoriaRepositorio.delete(suporte.buscarMercadoriaDaEmpresa(empresaId, mercadoriaId));
	}
}
