package com.autobots.automanager.servicos.venda;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.excecoes.ConflitoDeRecursoException;
import com.autobots.automanager.excecoes.RequisicaoInvalidaException;
import com.autobots.automanager.modelo.dto.venda.VendaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.venda.VendaCadastroDTO;
import com.autobots.automanager.modelo.dto.venda.VendaRespostaDTO;
import com.autobots.automanager.modelo.mapeadores.RespostaMapper;
import com.autobots.automanager.repositorios.VendaRepositorio;
import com.autobots.automanager.servicos.SuporteDominioServico;

@Service
public class VendaServico {

	@Autowired
	private VendaRepositorio vendaRepositorio;

	@Autowired
	private RespostaMapper respostaMapper;

	@Autowired
	private SuporteDominioServico suporte;

	public List<VendaRespostaDTO> listarDaEmpresa(Long empresaId) {
		suporte.validarEmpresaDoUsuarioAutenticado(empresaId);
		return suporte.buscarEmpresa(empresaId).getVendas().stream().filter(suporte::podeVisualizarVendaAutenticado)
				.map(respostaMapper::paraVenda)
				.collect(Collectors.toList());
	}

	public VendaRespostaDTO buscarDaEmpresa(Long empresaId, Long vendaId) {
		suporte.validarEmpresaDoUsuarioAutenticado(empresaId);
		Venda venda = suporte.buscarVendaDaEmpresa(empresaId, vendaId);
		suporte.validarVisualizacaoVenda(venda);
		return respostaMapper.paraVenda(venda);
	}

	public VendaRespostaDTO cadastrar(Long empresaId, VendaCadastroDTO dto) {
		suporte.validarEmpresaDoUsuarioAutenticado(empresaId);
		suporte.validarCriacaoVenda(dto.getFuncionarioId());
		validarIdentificacaoUnica(dto.getIdentificacao(), null);
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		Venda venda = new Venda();
		venda.setCadastro(new Date());
		venda.setEmpresa(empresa);
		aplicarRelacionamentos(empresaId, venda, dto.getIdentificacao(), dto.getClienteId(), dto.getFuncionarioId(),
				dto.getVeiculoId(), dto.getMercadoriasIds(), dto.getServicosIds());
		return respostaMapper.paraVenda(vendaRepositorio.save(venda));
	}

	public VendaRespostaDTO atualizar(Long empresaId, Long vendaId, VendaAtualizacaoDTO dto) {
		suporte.validarEmpresaDoUsuarioAutenticado(empresaId);
		Venda venda = suporte.buscarVendaDaEmpresa(empresaId, vendaId);
		if (dto.getIdentificacao() != null) {
			validarIdentificacaoUnica(dto.getIdentificacao(), vendaId);
		}
		aplicarRelacionamentos(empresaId, venda, dto.getIdentificacao(), dto.getClienteId(), dto.getFuncionarioId(),
				dto.getVeiculoId(), dto.getMercadoriasIds(), dto.getServicosIds());
		return respostaMapper.paraVenda(vendaRepositorio.save(venda));
	}

	public void remover(Long empresaId, Long vendaId) {
		suporte.validarEmpresaDoUsuarioAutenticado(empresaId);
		vendaRepositorio.delete(suporte.buscarVendaDaEmpresa(empresaId, vendaId));
	}

	private void aplicarRelacionamentos(Long empresaId, Venda venda, String identificacao, Long clienteId,
			Long funcionarioId, Long veiculoId, Set<Long> mercadoriasIds, Set<Long> servicosIds) {
		if (identificacao != null) {
			venda.setIdentificacao(identificacao);
		}
		if (clienteId != null) {
			Usuario cliente = suporte.buscarUsuarioDaEmpresa(empresaId, clienteId);
			suporte.validarPerfil(cliente, PerfilUsuario.CLIENTE);
			venda.setCliente(cliente);
		}
		if (funcionarioId != null) {
			Usuario funcionario = suporte.buscarUsuarioDaEmpresa(empresaId, funcionarioId);
			suporte.validarPerfil(funcionario, PerfilUsuario.VENDEDOR);
			venda.setFuncionario(funcionario);
		}
		if (veiculoId != null) {
			Veiculo veiculo = suporte.buscarVeiculo(veiculoId);
			venda.setVeiculo(veiculo);
		}
		if (mercadoriasIds != null) {
			venda.getMercadorias().clear();
			for (Long mercadoriaId : mercadoriasIds) {
				Mercadoria mercadoria = suporte.buscarMercadoriaDaEmpresa(empresaId, mercadoriaId);
				venda.getMercadorias().add(mercadoria);
			}
		}
		if (servicosIds != null) {
			venda.getServicos().clear();
			for (Long servicoId : servicosIds) {
				Servico servico = suporte.buscarServicoDaEmpresa(empresaId, servicoId);
				venda.getServicos().add(servico);
			}
		}
		validarVeiculoDoCliente(venda);
	}

	private void validarIdentificacaoUnica(String identificacao, Long vendaAtualId) {
		Optional<Venda> existente = vendaRepositorio.findByIdentificacao(identificacao);
		if (existente.isPresent() && !existente.get().getId().equals(vendaAtualId)) {
			throw new ConflitoDeRecursoException("JÃ¡ existe venda cadastrada com a identificaÃ§Ã£o informada");
		}
	}

	private void validarVeiculoDoCliente(Venda venda) {
		if (venda.getVeiculo() == null || venda.getCliente() == null) {
			return;
		}
		if (!venda.getVeiculo().getProprietario().getId().equals(venda.getCliente().getId())) {
			throw new RequisicaoInvalidaException("VeÃ­culo nÃ£o pertence ao cliente informado para a venda");
		}
	}
}
