package com.autobots.automanager.montadores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.modelo.dto.credencial.CredencialDTO;
import com.autobots.automanager.modelo.dto.documento.DocumentoDTO;
import com.autobots.automanager.modelo.dto.email.EmailDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.modelo.dto.veiculo.VeiculoDTO;

@Component
public class UsuarioSubrecursoModelAssembler {

	public EntityModel<EnderecoDTO> toEnderecoModel(Long usuarioId, EnderecoDTO endereco) {
		return EntityModel.of(endereco,
				linkTo(methodOn(UsuarioControle.class).buscarEndereco(usuarioId)).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).atualizarEndereco(usuarioId, new EnderecoDTO())).withRel("atualizar"),
				linkTo(methodOn(UsuarioControle.class).removerEndereco(usuarioId)).withRel("remover"));
	}

	public EntityModel<TelefoneDTO> toTelefoneModel(Long usuarioId, TelefoneDTO telefone) {
		return EntityModel.of(telefone,
				linkTo(methodOn(UsuarioControle.class).buscarTelefone(usuarioId, telefone.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).listarTelefones(usuarioId)).withRel("telefones"),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).atualizarTelefone(usuarioId, telefone.getId(), new TelefoneDTO())).withRel("atualizar"),
				linkTo(methodOn(UsuarioControle.class).removerTelefone(usuarioId, telefone.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<TelefoneDTO>> toTelefoneCollectionModel(Long usuarioId, List<TelefoneDTO> telefones) {
		List<EntityModel<TelefoneDTO>> modelos = telefones.stream().map(dto -> toTelefoneModel(usuarioId, dto))
				.collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(UsuarioControle.class).listarTelefones(usuarioId)).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).cadastrarTelefone(usuarioId, new TelefoneDTO())).withRel("cadastrar"));
	}

	public EntityModel<DocumentoDTO> toDocumentoModel(Long usuarioId, DocumentoDTO documento) {
		return EntityModel.of(documento,
				linkTo(methodOn(UsuarioControle.class).buscarDocumento(usuarioId, documento.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).listarDocumentos(usuarioId)).withRel("documentos"),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).atualizarDocumento(usuarioId, documento.getId(), new DocumentoDTO())).withRel("atualizar"),
				linkTo(methodOn(UsuarioControle.class).removerDocumento(usuarioId, documento.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<DocumentoDTO>> toDocumentoCollectionModel(Long usuarioId, List<DocumentoDTO> documentos) {
		List<EntityModel<DocumentoDTO>> modelos = documentos.stream().map(dto -> toDocumentoModel(usuarioId, dto))
				.collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(UsuarioControle.class).listarDocumentos(usuarioId)).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).cadastrarDocumento(usuarioId, new DocumentoDTO())).withRel("cadastrar"));
	}

	public EntityModel<EmailDTO> toEmailModel(Long usuarioId, EmailDTO email) {
		return EntityModel.of(email,
				linkTo(methodOn(UsuarioControle.class).buscarEmail(usuarioId, email.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).listarEmails(usuarioId)).withRel("emails"),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).atualizarEmail(usuarioId, email.getId(), new EmailDTO())).withRel("atualizar"),
				linkTo(methodOn(UsuarioControle.class).removerEmail(usuarioId, email.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<EmailDTO>> toEmailCollectionModel(Long usuarioId, List<EmailDTO> emails) {
		List<EntityModel<EmailDTO>> modelos = emails.stream().map(dto -> toEmailModel(usuarioId, dto))
				.collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(UsuarioControle.class).listarEmails(usuarioId)).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).cadastrarEmail(usuarioId, new EmailDTO())).withRel("cadastrar"));
	}

	public EntityModel<CredencialDTO> toCredencialModel(Long usuarioId, CredencialDTO credencial) {
		return EntityModel.of(credencial,
				linkTo(methodOn(UsuarioControle.class).buscarCredencial(usuarioId, credencial.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).listarCredenciais(usuarioId)).withRel("credenciais"),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).atualizarCredencial(usuarioId, credencial.getId(), new CredencialDTO())).withRel("atualizar"),
				linkTo(methodOn(UsuarioControle.class).removerCredencial(usuarioId, credencial.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<CredencialDTO>> toCredencialCollectionModel(Long usuarioId, List<CredencialDTO> credenciais) {
		List<EntityModel<CredencialDTO>> modelos = credenciais.stream().map(dto -> toCredencialModel(usuarioId, dto))
				.collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(UsuarioControle.class).listarCredenciais(usuarioId)).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).cadastrarCredencial(usuarioId, new CredencialDTO())).withRel("cadastrar"));
	}

	public EntityModel<VeiculoDTO> toVeiculoModel(Long usuarioId, VeiculoDTO veiculo) {
		return EntityModel.of(veiculo,
				linkTo(methodOn(UsuarioControle.class).buscarVeiculo(usuarioId, veiculo.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).listarVeiculos(usuarioId)).withRel("veiculos"),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).atualizarVeiculo(usuarioId, veiculo.getId(), new VeiculoDTO())).withRel("atualizar"),
				linkTo(methodOn(UsuarioControle.class).removerVeiculo(usuarioId, veiculo.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<VeiculoDTO>> toVeiculoCollectionModel(Long usuarioId, List<VeiculoDTO> veiculos) {
		List<EntityModel<VeiculoDTO>> modelos = veiculos.stream().map(dto -> toVeiculoModel(usuarioId, dto))
				.collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(UsuarioControle.class).listarVeiculos(usuarioId)).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuarioId)).withRel("usuario"),
				linkTo(methodOn(UsuarioControle.class).cadastrarVeiculo(usuarioId, new VeiculoDTO())).withRel("cadastrar"));
	}
}
