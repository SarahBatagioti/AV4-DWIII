package com.autobots.automanager.servicos;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.excecoes.RecursoNaoEncontradoException;
import com.autobots.automanager.excecoes.RequisicaoInvalidaException;
import com.autobots.automanager.repositorios.CredencialRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EmailRepositorio;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Service
public class SuporteDominioServico {

	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private TelefoneRepositorio telefoneRepositorio;

	@Autowired
	private DocumentoRepositorio documentoRepositorio;

	@Autowired
	private EmailRepositorio emailRepositorio;

	@Autowired
	private CredencialRepositorio credencialRepositorio;

	@Autowired
	private VeiculoRepositorio veiculoRepositorio;

	@Autowired
	private MercadoriaRepositorio mercadoriaRepositorio;

	@Autowired
	private ServicoRepositorio servicoRepositorio;

	@Autowired
	private VendaRepositorio vendaRepositorio;

	public Empresa buscarEmpresa(Long empresaId) {
		return empresaRepositorio.findById(empresaId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Empresa não encontrada"));
	}

	public Usuario buscarUsuario(Long usuarioId) {
		return usuarioRepositorio.findById(usuarioId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
	}

	public Usuario buscarUsuarioDaEmpresa(Long empresaId, Long usuarioId) {
		Usuario usuario = buscarUsuario(usuarioId);
		validarUsuarioDaEmpresa(empresaId, usuario);
		return usuario;
	}

	public Telefone buscarTelefone(Long telefoneId) {
		return telefoneRepositorio.findById(telefoneId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Telefone não encontrado"));
	}

	public Documento buscarDocumento(Long documentoId) {
		return documentoRepositorio.findById(documentoId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Documento não encontrado"));
	}

	public Email buscarEmail(Long emailId) {
		return emailRepositorio.findById(emailId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("E-mail não encontrado"));
	}

	public Credencial buscarCredencial(Long credencialId) {
		return credencialRepositorio.findById(credencialId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Credencial não encontrada"));
	}

	public Veiculo buscarVeiculo(Long veiculoId) {
		return veiculoRepositorio.findById(veiculoId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
	}

	public Veiculo buscarVeiculoDoUsuario(Long usuarioId, Long veiculoId) {
		Veiculo veiculo = buscarVeiculo(veiculoId);
		if (!veiculo.getProprietario().getId().equals(usuarioId)) {
			throw new RecursoNaoEncontradoException("Veículo não encontrado para o usuário informado");
		}
		return veiculo;
	}

	public Mercadoria buscarMercadoria(Long mercadoriaId) {
		return mercadoriaRepositorio.findById(mercadoriaId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Mercadoria não encontrada"));
	}

	public Mercadoria buscarMercadoriaDaEmpresa(Long empresaId, Long mercadoriaId) {
		Mercadoria mercadoria = buscarMercadoria(mercadoriaId);
		if (!mercadoria.getEmpresa().getId().equals(empresaId)) {
			throw new RecursoNaoEncontradoException("Mercadoria não encontrada para a empresa informada");
		}
		return mercadoria;
	}

	public Servico buscarServico(Long servicoId) {
		return servicoRepositorio.findById(servicoId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));
	}

	public Servico buscarServicoDaEmpresa(Long empresaId, Long servicoId) {
		Servico servico = buscarServico(servicoId);
		if (!servico.getEmpresa().getId().equals(empresaId)) {
			throw new RecursoNaoEncontradoException("Serviço não encontrado para a empresa informada");
		}
		return servico;
	}

	public Venda buscarVenda(Long vendaId) {
		return vendaRepositorio.findById(vendaId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Venda não encontrada"));
	}

	public Venda buscarVendaDaEmpresa(Long empresaId, Long vendaId) {
		Venda venda = buscarVenda(vendaId);
		if (!venda.getEmpresa().getId().equals(empresaId)) {
			throw new RecursoNaoEncontradoException("Venda não encontrada para a empresa informada");
		}
		return venda;
	}

	public void validarUsuarioDaEmpresa(Long empresaId, Usuario usuario) {
		if (!usuario.getEmpresa().getId().equals(empresaId)) {
			throw new RequisicaoInvalidaException("Usuário não pertence à empresa informada");
		}
	}

	public void validarPerfil(Usuario usuario, PerfilUsuario perfil) {
		if (!usuario.getPerfis().contains(perfil)) {
			throw new RequisicaoInvalidaException("Usuário não possui o perfil exigido: " + perfil.name());
		}
	}

	public void validarDocumentoDoUsuario(Long usuarioId, Documento documento) {
		if (documento.getUsuario() == null || !documento.getUsuario().getId().equals(usuarioId)) {
			throw new RecursoNaoEncontradoException("Documento não encontrado para o usuário informado");
		}
	}

	public void validarEmailDoUsuario(Long usuarioId, Email email) {
		if (email.getUsuario() == null || !email.getUsuario().getId().equals(usuarioId)) {
			throw new RecursoNaoEncontradoException("E-mail não encontrado para o usuário informado");
		}
	}

	public void validarCredencialDoUsuario(Long usuarioId, Credencial credencial) {
		if (credencial.getUsuario() == null || !credencial.getUsuario().getId().equals(usuarioId)) {
			throw new RecursoNaoEncontradoException("Credencial não encontrada para o usuário informado");
		}
	}

	public void validarTelefoneDaEmpresa(Long empresaId, Telefone telefone) {
		if (telefone.getEmpresa() == null || !telefone.getEmpresa().getId().equals(empresaId)) {
			throw new RecursoNaoEncontradoException("Telefone não encontrado para a empresa informada");
		}
	}

	public void validarTelefoneDoUsuario(Long usuarioId, Telefone telefone) {
		if (telefone.getUsuario() == null || !telefone.getUsuario().getId().equals(usuarioId)) {
			throw new RecursoNaoEncontradoException("Telefone não encontrado para o usuário informado");
		}
	}

	public Optional<Usuario> fornecedorDaEmpresa(Long empresaId, Long fornecedorId) {
		if (fornecedorId == null) {
			return Optional.empty();
		}
		Usuario fornecedor = buscarUsuarioDaEmpresa(empresaId, fornecedorId);
		validarPerfil(fornecedor, PerfilUsuario.FORNECEDOR);
		return Optional.of(fornecedor);
	}
}
