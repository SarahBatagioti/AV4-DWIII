package com.autobots.automanager.servicos;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.autobots.automanager.servicos.autenticacao.AutenticacaoUsuarioServico;

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

	@Autowired
	private AutenticacaoUsuarioServico autenticacaoUsuarioServico;

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

	public Usuario buscarUsuarioAutenticado() {
		Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
		if (autenticacao == null || !autenticacao.isAuthenticated() || autenticacao.getName() == null
				|| "anonymousUser".equalsIgnoreCase(autenticacao.getName())) {
			throw new AccessDeniedException("Autenticação obrigatória");
		}
		return autenticacaoUsuarioServico.buscarUsuarioAtivoPorNomeUsuario(autenticacao.getName());
	}

	public boolean usuarioAutenticadoPossuiPerfil(PerfilUsuario perfil) {
		return buscarUsuarioAutenticado().getPerfis().contains(perfil);
	}

	public void validarEmpresaDoUsuarioAutenticado(Long empresaId) {
		if (usuarioAutenticadoPossuiPerfil(PerfilUsuario.ADMINISTRADOR)) {
			return;
		}
		Usuario usuarioAutenticado = buscarUsuarioAutenticado();
		if (usuarioAutenticado.getEmpresa() == null || !usuarioAutenticado.getEmpresa().getId().equals(empresaId)) {
			throw new AccessDeniedException("Usuário autenticado não pertence à empresa informada");
		}
	}

	public boolean podeVisualizarUsuarioAutenticado(Usuario usuario) {
		try {
			validarLeituraUsuario(usuario);
			return true;
		} catch (AccessDeniedException ex) {
			return false;
		}
	}

	public void validarLeituraUsuario(Usuario usuario) {
		Usuario usuarioAutenticado = buscarUsuarioAutenticado();
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.ADMINISTRADOR)) {
			return;
		}
		validarMesmoEmpresa(usuarioAutenticado, usuario);
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.GERENTE)) {
			validarPerfisGerenciaveis(usuario.getPerfis(),
					Set.of(PerfilUsuario.GERENTE, PerfilUsuario.VENDEDOR, PerfilUsuario.CLIENTE),
					"Gerente não pode acessar usuários administradores");
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.VENDEDOR)) {
			validarUsuarioSomenteCliente(usuario.getPerfis(), "Vendedor só pode acessar usuários clientes");
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.CLIENTE)
				&& usuarioAutenticado.getId().equals(usuario.getId())) {
			return;
		}
		throw new AccessDeniedException("Usuário autenticado não pode acessar o cadastro informado");
	}

	public void validarCadastroUsuario(Long empresaId, Set<PerfilUsuario> perfis) {
		validarEmpresaDoUsuarioAutenticado(empresaId);
		Usuario usuarioAutenticado = buscarUsuarioAutenticado();
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.ADMINISTRADOR)) {
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.GERENTE)) {
			validarPerfisGerenciaveis(perfis,
					Set.of(PerfilUsuario.GERENTE, PerfilUsuario.VENDEDOR, PerfilUsuario.CLIENTE),
					"Gerente não pode cadastrar usuários administradores");
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.VENDEDOR)) {
			validarUsuarioSomenteCliente(perfis, "Vendedor só pode cadastrar usuários clientes");
			return;
		}
		throw new AccessDeniedException("Usuário autenticado não pode cadastrar usuários");
	}

	public void validarGerenciamentoUsuario(Usuario usuario, Set<PerfilUsuario> novosPerfis) {
		Usuario usuarioAutenticado = buscarUsuarioAutenticado();
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.ADMINISTRADOR)) {
			return;
		}
		validarMesmoEmpresa(usuarioAutenticado, usuario);
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.GERENTE)) {
			validarPerfisGerenciaveis(usuario.getPerfis(),
					Set.of(PerfilUsuario.GERENTE, PerfilUsuario.VENDEDOR, PerfilUsuario.CLIENTE),
					"Gerente não pode alterar usuários administradores");
			if (novosPerfis != null && !novosPerfis.isEmpty()) {
				validarPerfisGerenciaveis(novosPerfis,
						Set.of(PerfilUsuario.GERENTE, PerfilUsuario.VENDEDOR, PerfilUsuario.CLIENTE),
						"Gerente não pode atribuir perfil administrador");
			}
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.VENDEDOR)) {
			validarUsuarioSomenteCliente(usuario.getPerfis(), "Vendedor só pode alterar usuários clientes");
			if (novosPerfis != null && !novosPerfis.isEmpty()) {
				validarUsuarioSomenteCliente(novosPerfis, "Vendedor só pode manter o perfil cliente");
			}
			return;
		}
		throw new AccessDeniedException("Usuário autenticado não pode alterar o cadastro informado");
	}

	public boolean podeVisualizarVendaAutenticado(Venda venda) {
		try {
			validarVisualizacaoVenda(venda);
			return true;
		} catch (AccessDeniedException ex) {
			return false;
		}
	}

	public void validarVisualizacaoVenda(Venda venda) {
		Usuario usuarioAutenticado = buscarUsuarioAutenticado();
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.ADMINISTRADOR)) {
			return;
		}
		validarEmpresaDoUsuarioAutenticado(venda.getEmpresa().getId());
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.GERENTE)) {
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.VENDEDOR)
				&& venda.getFuncionario() != null
				&& usuarioAutenticado.getId().equals(venda.getFuncionario().getId())) {
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.CLIENTE)
				&& venda.getCliente() != null
				&& usuarioAutenticado.getId().equals(venda.getCliente().getId())) {
			return;
		}
		throw new AccessDeniedException("Usuário autenticado não pode acessar a venda informada");
	}

	public void validarCriacaoVenda(Long funcionarioId) {
		Usuario usuarioAutenticado = buscarUsuarioAutenticado();
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.ADMINISTRADOR)
				|| usuarioAutenticado.getPerfis().contains(PerfilUsuario.GERENTE)) {
			return;
		}
		if (usuarioAutenticado.getPerfis().contains(PerfilUsuario.VENDEDOR)
				&& usuarioAutenticado.getId().equals(funcionarioId)) {
			return;
		}
		throw new AccessDeniedException("Vendedor só pode criar vendas feitas por si mesmo");
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
		return Optional.of(fornecedor);
	}

	private void validarMesmoEmpresa(Usuario usuarioAutenticado, Usuario usuarioAlvo) {
		if (usuarioAutenticado.getEmpresa() == null || usuarioAlvo.getEmpresa() == null
				|| !usuarioAutenticado.getEmpresa().getId().equals(usuarioAlvo.getEmpresa().getId())) {
			throw new AccessDeniedException("Usuário autenticado não pertence à mesma empresa do cadastro informado");
		}
	}

	private void validarPerfisGerenciaveis(Set<PerfilUsuario> perfis, Set<PerfilUsuario> permitidos, String mensagem) {
		if (perfis == null || perfis.isEmpty() || !permitidos.containsAll(perfis)) {
			throw new AccessDeniedException(mensagem);
		}
	}

	private void validarUsuarioSomenteCliente(Set<PerfilUsuario> perfis, String mensagem) {
		if (perfis == null || perfis.size() != 1 || !perfis.contains(PerfilUsuario.CLIENTE)) {
			throw new AccessDeniedException(mensagem);
		}
	}
}
