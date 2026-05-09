package com.autobots.automanager.servicos.usuario;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.excecoes.ConflitoDeRecursoException;
import com.autobots.automanager.excecoes.RecursoNaoEncontradoException;
import com.autobots.automanager.excecoes.RequisicaoInvalidaException;
import com.autobots.automanager.modelo.atualizadores.credencial.CredencialAtualizador;
import com.autobots.automanager.modelo.atualizadores.documento.DocumentoAtualizador;
import com.autobots.automanager.modelo.atualizadores.email.EmailAtualizador;
import com.autobots.automanager.modelo.atualizadores.endereco.EnderecoAtualizador;
import com.autobots.automanager.modelo.atualizadores.telefone.TelefoneAtualizador;
import com.autobots.automanager.modelo.atualizadores.usuario.UsuarioAtualizador;
import com.autobots.automanager.modelo.atualizadores.veiculo.VeiculoAtualizador;
import com.autobots.automanager.modelo.dto.credencial.CredencialDTO;
import com.autobots.automanager.modelo.dto.documento.DocumentoDTO;
import com.autobots.automanager.modelo.dto.email.EmailDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioCadastroDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioRespostaDTO;
import com.autobots.automanager.modelo.dto.veiculo.VeiculoDTO;
import com.autobots.automanager.modelo.mapeadores.RespostaMapper;
import com.autobots.automanager.repositorios.CredencialCodigoBarraRepositorio;
import com.autobots.automanager.repositorios.CredencialRepositorio;
import com.autobots.automanager.repositorios.CredencialUsuarioSenhaRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EmailRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.servicos.SuporteDominioServico;

@Service
public class UsuarioServico {

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
	private CredencialUsuarioSenhaRepositorio credencialUsuarioSenhaRepositorio;

	@Autowired
	private CredencialCodigoBarraRepositorio credencialCodigoBarraRepositorio;

	@Autowired
	private VeiculoRepositorio veiculoRepositorio;

	@Autowired
	private UsuarioAtualizador usuarioAtualizador;

	@Autowired
	private EnderecoAtualizador enderecoAtualizador;

	@Autowired
	private TelefoneAtualizador telefoneAtualizador;

	@Autowired
	private DocumentoAtualizador documentoAtualizador;

	@Autowired
	private EmailAtualizador emailAtualizador;

	@Autowired
	private CredencialAtualizador credencialAtualizador;

	@Autowired
	private VeiculoAtualizador veiculoAtualizador;

	@Autowired
	private RespostaMapper respostaMapper;

	@Autowired
	private SuporteDominioServico suporte;

	public List<UsuarioRespostaDTO> listarDaEmpresa(Long empresaId) {
		return suporte.buscarEmpresa(empresaId).getUsuarios().stream().sorted(Comparator.comparing(Usuario::getId))
				.map(respostaMapper::paraUsuario).collect(Collectors.toList());
	}

	public UsuarioRespostaDTO buscarPorId(Long usuarioId) {
		return respostaMapper.paraUsuario(suporte.buscarUsuario(usuarioId));
	}

	public UsuarioRespostaDTO buscarDaEmpresa(Long empresaId, Long usuarioId) {
		return respostaMapper.paraUsuario(suporte.buscarUsuarioDaEmpresa(empresaId, usuarioId));
	}

	@Transactional
	public UsuarioRespostaDTO cadastrar(Long empresaId, UsuarioCadastroDTO dto) {
		Usuario usuario = usuarioAtualizador.criar(dto);
		usuario.setEmpresa(suporte.buscarEmpresa(empresaId));
		if (dto.getEndereco() != null) {
			usuario.setEndereco(enderecoAtualizador.criar(dto.getEndereco()));
		}
		anexarTelefones(usuario, dto.getTelefones());
		anexarDocumentos(usuario, dto.getDocumentos());
		anexarEmails(usuario, dto.getEmails());
		anexarCredenciais(usuario, dto.getCredenciais());
		anexarVeiculos(usuario, dto.getVeiculos());
		return respostaMapper.paraUsuario(usuarioRepositorio.save(usuario));
	}

	@Transactional
	public UsuarioRespostaDTO atualizar(Long empresaId, Long usuarioId, UsuarioAtualizacaoDTO dto) {
		Usuario usuario = suporte.buscarUsuarioDaEmpresa(empresaId, usuarioId);
		return atualizarInterno(usuario, dto);
	}

	@Transactional
	public UsuarioRespostaDTO atualizar(Long usuarioId, UsuarioAtualizacaoDTO dto) {
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		return atualizarInterno(usuario, dto);
	}

	private UsuarioRespostaDTO atualizarInterno(Usuario usuario, UsuarioAtualizacaoDTO dto) {
		usuarioAtualizador.atualizar(usuario, dto);
		if (dto.getEndereco() != null) {
			if (usuario.getEndereco() == null) {
				usuario.setEndereco(enderecoAtualizador.criar(dto.getEndereco()));
			} else {
				enderecoAtualizador.atualizar(usuario.getEndereco(), dto.getEndereco());
			}
		}
		return respostaMapper.paraUsuario(usuarioRepositorio.save(usuario));
	}

	public void remover(Long empresaId, Long usuarioId) {
		usuarioRepositorio.delete(suporte.buscarUsuarioDaEmpresa(empresaId, usuarioId));
	}

	public void remover(Long usuarioId) {
		usuarioRepositorio.delete(suporte.buscarUsuario(usuarioId));
	}

	public EnderecoDTO buscarEndereco(Long usuarioId) {
		Endereco endereco = suporte.buscarUsuario(usuarioId).getEndereco();
		if (endereco == null) {
			throw new RecursoNaoEncontradoException("Endereço não cadastrado para o usuário");
		}
		return respostaMapper.paraEndereco(endereco);
	}

	@Transactional
	public EnderecoDTO cadastrarEndereco(Long usuarioId, EnderecoDTO dto) {
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		if (usuario.getEndereco() != null) {
			throw new ConflitoDeRecursoException("Usuário já possui endereço cadastrado");
		}
		usuario.setEndereco(enderecoAtualizador.criar(dto));
		usuarioRepositorio.save(usuario);
		return respostaMapper.paraEndereco(usuario.getEndereco());
	}

	@Transactional
	public EnderecoDTO atualizarEndereco(Long usuarioId, EnderecoDTO dto) {
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		if (usuario.getEndereco() == null) {
			usuario.setEndereco(enderecoAtualizador.criar(dto));
		} else {
			enderecoAtualizador.atualizar(usuario.getEndereco(), dto);
		}
		usuarioRepositorio.save(usuario);
		return respostaMapper.paraEndereco(usuario.getEndereco());
	}

	@Transactional
	public void removerEndereco(Long usuarioId) {
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		if (usuario.getEndereco() == null) {
			throw new RecursoNaoEncontradoException("Endereço não cadastrado para o usuário");
		}
		usuario.setEndereco(null);
		usuarioRepositorio.save(usuario);
	}

	public List<TelefoneDTO> listarTelefones(Long usuarioId) {
		return respostaMapper.paraTelefones(suporte.buscarUsuario(usuarioId).getTelefones());
	}

	public TelefoneDTO buscarTelefone(Long usuarioId, Long telefoneId) {
		Telefone telefone = suporte.buscarTelefone(telefoneId);
		suporte.validarTelefoneDoUsuario(usuarioId, telefone);
		return respostaMapper.paraTelefone(telefone);
	}

	@Transactional
	public TelefoneDTO cadastrarTelefone(Long usuarioId, TelefoneDTO dto) {
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		Telefone telefone = telefoneAtualizador.criar(dto);
		telefone.setUsuario(usuario);
		return respostaMapper.paraTelefone(telefoneRepositorio.save(telefone));
	}

	@Transactional
	public TelefoneDTO atualizarTelefone(Long usuarioId, Long telefoneId, TelefoneDTO dto) {
		Telefone telefone = suporte.buscarTelefone(telefoneId);
		suporte.validarTelefoneDoUsuario(usuarioId, telefone);
		telefoneAtualizador.atualizar(telefone, dto);
		return respostaMapper.paraTelefone(telefoneRepositorio.save(telefone));
	}

	public void removerTelefone(Long usuarioId, Long telefoneId) {
		Telefone telefone = suporte.buscarTelefone(telefoneId);
		suporte.validarTelefoneDoUsuario(usuarioId, telefone);
		telefoneRepositorio.delete(telefone);
	}

	public List<DocumentoDTO> listarDocumentos(Long usuarioId) {
		return respostaMapper.paraDocumentos(suporte.buscarUsuario(usuarioId).getDocumentos());
	}

	public DocumentoDTO buscarDocumento(Long usuarioId, Long documentoId) {
		Documento documento = suporte.buscarDocumento(documentoId);
		suporte.validarDocumentoDoUsuario(usuarioId, documento);
		return respostaMapper.paraDocumento(documento);
	}

	@Transactional
	public DocumentoDTO cadastrarDocumento(Long usuarioId, DocumentoDTO dto) {
		validarNumeroDocumentoUnico(dto.getNumero(), null);
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		Documento documento = documentoAtualizador.criar(dto);
		documento.setUsuario(usuario);
		return respostaMapper.paraDocumento(documentoRepositorio.save(documento));
	}

	@Transactional
	public DocumentoDTO atualizarDocumento(Long usuarioId, Long documentoId, DocumentoDTO dto) {
		Documento documento = suporte.buscarDocumento(documentoId);
		suporte.validarDocumentoDoUsuario(usuarioId, documento);
		validarNumeroDocumentoUnico(dto.getNumero(), documentoId);
		documentoAtualizador.atualizar(documento, dto);
		return respostaMapper.paraDocumento(documentoRepositorio.save(documento));
	}

	public void removerDocumento(Long usuarioId, Long documentoId) {
		Documento documento = suporte.buscarDocumento(documentoId);
		suporte.validarDocumentoDoUsuario(usuarioId, documento);
		documentoRepositorio.delete(documento);
	}

	public List<EmailDTO> listarEmails(Long usuarioId) {
		return respostaMapper.paraEmails(suporte.buscarUsuario(usuarioId).getEmails());
	}

	public EmailDTO buscarEmail(Long usuarioId, Long emailId) {
		Email email = suporte.buscarEmail(emailId);
		suporte.validarEmailDoUsuario(usuarioId, email);
		return respostaMapper.paraEmail(email);
	}

	@Transactional
	public EmailDTO cadastrarEmail(Long usuarioId, EmailDTO dto) {
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		Email email = emailAtualizador.criar(dto);
		email.setUsuario(usuario);
		return respostaMapper.paraEmail(emailRepositorio.save(email));
	}

	@Transactional
	public EmailDTO atualizarEmail(Long usuarioId, Long emailId, EmailDTO dto) {
		Email email = suporte.buscarEmail(emailId);
		suporte.validarEmailDoUsuario(usuarioId, email);
		emailAtualizador.atualizar(email, dto);
		return respostaMapper.paraEmail(emailRepositorio.save(email));
	}

	public void removerEmail(Long usuarioId, Long emailId) {
		Email email = suporte.buscarEmail(emailId);
		suporte.validarEmailDoUsuario(usuarioId, email);
		emailRepositorio.delete(email);
	}

	public List<CredencialDTO> listarCredenciais(Long usuarioId) {
		return respostaMapper.paraCredenciais(suporte.buscarUsuario(usuarioId).getCredenciais());
	}

	public CredencialDTO buscarCredencial(Long usuarioId, Long credencialId) {
		Credencial credencial = suporte.buscarCredencial(credencialId);
		suporte.validarCredencialDoUsuario(usuarioId, credencial);
		return respostaMapper.paraCredencial(credencial);
	}

	@Transactional
	public CredencialDTO cadastrarCredencial(Long usuarioId, CredencialDTO dto) {
		validarCredencialUnica(dto, null);
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		Credencial credencial = credencialAtualizador.criar(dto);
		credencial.setUsuario(usuario);
		return respostaMapper.paraCredencial(credencialRepositorio.save(credencial));
	}

	@Transactional
	public CredencialDTO atualizarCredencial(Long usuarioId, Long credencialId, CredencialDTO dto) {
		Credencial credencial = suporte.buscarCredencial(credencialId);
		suporte.validarCredencialDoUsuario(usuarioId, credencial);
		if (!credencialAtualizador.mesmoTipo(credencial, dto.getTipo())) {
			throw new RequisicaoInvalidaException("Não é permitido alterar o tipo da credencial");
		}
		validarCredencialUnica(dto, credencialId);
		credencialAtualizador.atualizar(credencial, dto);
		return respostaMapper.paraCredencial(credencialRepositorio.save(credencial));
	}

	public void removerCredencial(Long usuarioId, Long credencialId) {
		Credencial credencial = suporte.buscarCredencial(credencialId);
		suporte.validarCredencialDoUsuario(usuarioId, credencial);
		credencialRepositorio.delete(credencial);
	}

	public List<VeiculoDTO> listarVeiculos(Long usuarioId) {
		return respostaMapper.paraVeiculos(suporte.buscarUsuario(usuarioId).getVeiculos());
	}

	public VeiculoDTO buscarVeiculo(Long usuarioId, Long veiculoId) {
		return respostaMapper.paraVeiculo(suporte.buscarVeiculoDoUsuario(usuarioId, veiculoId));
	}

	@Transactional
	public VeiculoDTO cadastrarVeiculo(Long usuarioId, VeiculoDTO dto) {
		Usuario usuario = suporte.buscarUsuario(usuarioId);
		suporte.validarPerfil(usuario, PerfilUsuario.CLIENTE);
		Veiculo veiculo = veiculoAtualizador.criar(dto);
		veiculo.setProprietario(usuario);
		return respostaMapper.paraVeiculo(veiculoRepositorio.save(veiculo));
	}

	@Transactional
	public VeiculoDTO atualizarVeiculo(Long usuarioId, Long veiculoId, VeiculoDTO dto) {
		Veiculo veiculo = suporte.buscarVeiculoDoUsuario(usuarioId, veiculoId);
		veiculoAtualizador.atualizar(veiculo, dto);
		return respostaMapper.paraVeiculo(veiculoRepositorio.save(veiculo));
	}

	public void removerVeiculo(Long usuarioId, Long veiculoId) {
		veiculoRepositorio.delete(suporte.buscarVeiculoDoUsuario(usuarioId, veiculoId));
	}

	private void anexarTelefones(Usuario usuario, List<TelefoneDTO> telefones) {
		if (telefones == null) {
			return;
		}
		for (TelefoneDTO dto : telefones) {
			Telefone telefone = telefoneAtualizador.criar(dto);
			telefone.setUsuario(usuario);
			usuario.getTelefones().add(telefone);
		}
	}

	private void anexarDocumentos(Usuario usuario, List<DocumentoDTO> documentos) {
		if (documentos == null) {
			return;
		}
		for (DocumentoDTO dto : documentos) {
			validarNumeroDocumentoUnico(dto.getNumero(), null);
			Documento documento = documentoAtualizador.criar(dto);
			documento.setUsuario(usuario);
			usuario.getDocumentos().add(documento);
		}
	}

	private void anexarEmails(Usuario usuario, List<EmailDTO> emails) {
		if (emails == null) {
			return;
		}
		for (EmailDTO dto : emails) {
			Email email = emailAtualizador.criar(dto);
			email.setUsuario(usuario);
			usuario.getEmails().add(email);
		}
	}

	private void anexarCredenciais(Usuario usuario, List<CredencialDTO> credenciais) {
		if (credenciais == null) {
			return;
		}
		for (CredencialDTO dto : credenciais) {
			validarCredencialUnica(dto, null);
			Credencial credencial = credencialAtualizador.criar(dto);
			credencial.setUsuario(usuario);
			usuario.getCredenciais().add(credencial);
		}
	}

	private void anexarVeiculos(Usuario usuario, List<VeiculoDTO> veiculos) {
		if (veiculos == null || veiculos.isEmpty()) {
			return;
		}
		suporte.validarPerfil(usuario, PerfilUsuario.CLIENTE);
		for (VeiculoDTO dto : veiculos) {
			Veiculo veiculo = veiculoAtualizador.criar(dto);
			veiculo.setProprietario(usuario);
			usuario.getVeiculos().add(veiculo);
		}
	}

	private void validarNumeroDocumentoUnico(String numero, Long documentoAtualId) {
		Optional<Documento> existente = documentoRepositorio.findByNumero(numero);
		if (existente.isPresent() && !existente.get().getId().equals(documentoAtualId)) {
			throw new ConflitoDeRecursoException("Já existe documento cadastrado com o número informado");
		}
	}

	private void validarCredencialUnica(CredencialDTO dto, Long credencialAtualId) {
		if ("usuario-senha".equalsIgnoreCase(dto.getTipo()) && dto.getNomeUsuario() != null) {
			credencialUsuarioSenhaRepositorio.findByNomeUsuario(dto.getNomeUsuario()).ifPresent(existente -> {
				if (!existente.getId().equals(credencialAtualId)) {
					throw new ConflitoDeRecursoException("Já existe credencial cadastrada com o nome de usuário informado");
				}
			});
			return;
		}
		if ("codigo-barra".equalsIgnoreCase(dto.getTipo()) && dto.getCodigo() != null) {
			credencialCodigoBarraRepositorio.findByCodigo(dto.getCodigo()).ifPresent(existente -> {
				if (!existente.getId().equals(credencialAtualId)) {
					throw new ConflitoDeRecursoException("Já existe credencial cadastrada com o código informado");
				}
			});
		}
	}
}
