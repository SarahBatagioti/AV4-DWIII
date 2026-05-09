package com.autobots.automanager.modelo.mapeadores;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.dto.credencial.CredencialDTO;
import com.autobots.automanager.modelo.dto.documento.DocumentoDTO;
import com.autobots.automanager.modelo.dto.email.EmailDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaRespostaDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaRespostaDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoRespostaDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioRespostaDTO;
import com.autobots.automanager.modelo.dto.veiculo.VeiculoDTO;
import com.autobots.automanager.modelo.dto.venda.VendaRespostaDTO;

@Component
public class RespostaMapper {

	public EmpresaRespostaDTO paraEmpresa(Empresa empresa) {
		EmpresaRespostaDTO dto = new EmpresaRespostaDTO();
		dto.setId(empresa.getId());
		dto.setRazaoSocial(empresa.getRazaoSocial());
		dto.setNomeFantasia(empresa.getNomeFantasia());
		dto.setCadastro(empresa.getCadastro());
		dto.setEndereco(paraEndereco(empresa.getEndereco()));
		dto.setTelefones(paraTelefones(empresa.getTelefones()));
		dto.setUsuariosIds(listaOrdenadaIds(empresa.getUsuarios().stream().map(Usuario::getId).collect(Collectors.toSet())));
		dto.setMercadoriasIds(listaOrdenadaIds(empresa.getMercadorias().stream().map(Mercadoria::getId).collect(Collectors.toSet())));
		dto.setServicosIds(listaOrdenadaIds(empresa.getServicos().stream().map(Servico::getId).collect(Collectors.toSet())));
		dto.setVendasIds(listaOrdenadaIds(empresa.getVendas().stream().map(Venda::getId).collect(Collectors.toSet())));
		return dto;
	}

	public UsuarioRespostaDTO paraUsuario(Usuario usuario) {
		UsuarioRespostaDTO dto = new UsuarioRespostaDTO();
		dto.setId(usuario.getId());
		dto.setEmpresaId(usuario.getEmpresa().getId());
		dto.setNome(usuario.getNome());
		dto.setNomeSocial(usuario.getNomeSocial());
		dto.setDataNascimento(usuario.getDataNascimento());
		dto.setDataCadastro(usuario.getDataCadastro());
		dto.setPerfis(usuario.getPerfis());
		dto.setEndereco(paraEndereco(usuario.getEndereco()));
		dto.setTelefones(paraTelefones(usuario.getTelefones()));
		dto.setDocumentos(paraDocumentos(usuario.getDocumentos()));
		dto.setEmails(paraEmails(usuario.getEmails()));
		dto.setCredenciais(paraCredenciais(usuario.getCredenciais()));
		dto.setVeiculos(paraVeiculos(usuario.getVeiculos()));
		return dto;
	}

	public MercadoriaRespostaDTO paraMercadoria(Mercadoria mercadoria) {
		MercadoriaRespostaDTO dto = new MercadoriaRespostaDTO();
		dto.setId(mercadoria.getId());
		dto.setEmpresaId(mercadoria.getEmpresa().getId());
		dto.setFornecedorId(mercadoria.getFornecedor() == null ? null : mercadoria.getFornecedor().getId());
		dto.setValidade(mercadoria.getValidade());
		dto.setFabricao(mercadoria.getFabricao());
		dto.setCadastro(mercadoria.getCadastro());
		dto.setNome(mercadoria.getNome());
		dto.setQuantidade(mercadoria.getQuantidade());
		dto.setValor(mercadoria.getValor());
		dto.setDescricao(mercadoria.getDescricao());
		return dto;
	}

	public ServicoRespostaDTO paraServico(Servico servico) {
		ServicoRespostaDTO dto = new ServicoRespostaDTO();
		dto.setId(servico.getId());
		dto.setEmpresaId(servico.getEmpresa().getId());
		dto.setNome(servico.getNome());
		dto.setValor(servico.getValor());
		dto.setDescricao(servico.getDescricao());
		return dto;
	}

	public VendaRespostaDTO paraVenda(Venda venda) {
		VendaRespostaDTO dto = new VendaRespostaDTO();
		dto.setId(venda.getId());
		dto.setEmpresaId(venda.getEmpresa().getId());
		dto.setCadastro(venda.getCadastro());
		dto.setIdentificacao(venda.getIdentificacao());
		dto.setClienteId(venda.getCliente().getId());
		dto.setFuncionarioId(venda.getFuncionario().getId());
		dto.setVeiculoId(venda.getVeiculo() == null ? null : venda.getVeiculo().getId());
		dto.setMercadoriasIds(conjuntoOrdenadoIds(venda.getMercadorias().stream().map(Mercadoria::getId).collect(Collectors.toSet())));
		dto.setServicosIds(conjuntoOrdenadoIds(venda.getServicos().stream().map(Servico::getId).collect(Collectors.toSet())));
		return dto;
	}

	public EnderecoDTO paraEndereco(Endereco endereco) {
		if (endereco == null) {
			return null;
		}
		EnderecoDTO dto = new EnderecoDTO();
		dto.setId(endereco.getId());
		dto.setEstado(endereco.getEstado());
		dto.setCidade(endereco.getCidade());
		dto.setBairro(endereco.getBairro());
		dto.setRua(endereco.getRua());
		dto.setNumero(endereco.getNumero());
		dto.setCodigoPostal(endereco.getCodigoPostal());
		dto.setInformacoesAdicionais(endereco.getInformacoesAdicionais());
		return dto;
	}

	public TelefoneDTO paraTelefone(Telefone telefone) {
		TelefoneDTO dto = new TelefoneDTO();
		dto.setId(telefone.getId());
		dto.setDdd(telefone.getDdd());
		dto.setNumero(telefone.getNumero());
		return dto;
	}

	public DocumentoDTO paraDocumento(Documento documento) {
		DocumentoDTO dto = new DocumentoDTO();
		dto.setId(documento.getId());
		dto.setTipo(documento.getTipo());
		dto.setDataEmissao(documento.getDataEmissao());
		dto.setNumero(documento.getNumero());
		return dto;
	}

	public EmailDTO paraEmail(Email email) {
		EmailDTO dto = new EmailDTO();
		dto.setId(email.getId());
		dto.setEndereco(email.getEndereco());
		return dto;
	}

	public CredencialDTO paraCredencial(Credencial credencial) {
		CredencialDTO dto = new CredencialDTO();
		dto.setId(credencial.getId());
		dto.setCriacao(credencial.getCriacao());
		dto.setUltimoAcesso(credencial.getUltimoAcesso());
		dto.setInativo(credencial.isInativo());
		if (credencial instanceof CredencialUsuarioSenha) {
			CredencialUsuarioSenha atual = (CredencialUsuarioSenha) credencial;
			dto.setTipo("usuario-senha");
			dto.setNomeUsuario(atual.getNomeUsuario());
			dto.setSenha(atual.getSenha());
		} else if (credencial instanceof CredencialCodigoBarra) {
			CredencialCodigoBarra atual = (CredencialCodigoBarra) credencial;
			dto.setTipo("codigo-barra");
			dto.setCodigo(atual.getCodigo());
		}
		return dto;
	}

	public VeiculoDTO paraVeiculo(Veiculo veiculo) {
		VeiculoDTO dto = new VeiculoDTO();
		dto.setId(veiculo.getId());
		dto.setTipo(veiculo.getTipo());
		dto.setModelo(veiculo.getModelo());
		dto.setPlaca(veiculo.getPlaca());
		return dto;
	}

	public List<TelefoneDTO> paraTelefones(Set<Telefone> telefones) {
		return telefones.stream().sorted(Comparator.comparing(Telefone::getId)).map(this::paraTelefone)
				.collect(Collectors.toList());
	}

	public List<DocumentoDTO> paraDocumentos(Set<Documento> documentos) {
		return documentos.stream().sorted(Comparator.comparing(Documento::getId)).map(this::paraDocumento)
				.collect(Collectors.toList());
	}

	public List<EmailDTO> paraEmails(Set<Email> emails) {
		return emails.stream().sorted(Comparator.comparing(Email::getId)).map(this::paraEmail).collect(Collectors.toList());
	}

	public List<CredencialDTO> paraCredenciais(Set<Credencial> credenciais) {
		return credenciais.stream().sorted(Comparator.comparing(Credencial::getId)).map(this::paraCredencial)
				.collect(Collectors.toList());
	}

	public List<VeiculoDTO> paraVeiculos(Set<Veiculo> veiculos) {
		return veiculos.stream().sorted(Comparator.comparing(Veiculo::getId)).map(this::paraVeiculo)
				.collect(Collectors.toList());
	}

	private List<Long> listaOrdenadaIds(Set<Long> ids) {
		return ids.stream().sorted().collect(Collectors.toList());
	}

	private Set<Long> conjuntoOrdenadoIds(Set<Long> ids) {
		return ids.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
