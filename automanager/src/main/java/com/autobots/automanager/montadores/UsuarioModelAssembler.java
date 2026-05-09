package com.autobots.automanager.montadores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.modelo.dto.usuario.UsuarioAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioCadastroDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioRespostaDTO;

@Component
public class UsuarioModelAssembler {

	public EntityModel<UsuarioRespostaDTO> toModel(UsuarioRespostaDTO usuario) {
		return EntityModel.of(usuario,
				linkTo(methodOn(UsuarioControle.class).buscarPorId(usuario.getId())).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).listarDaEmpresa(usuario.getEmpresaId())).withRel("usuarios"),
				linkTo(methodOn(UsuarioControle.class).buscarEndereco(usuario.getId())).withRel("endereco"),
				linkTo(methodOn(UsuarioControle.class).listarTelefones(usuario.getId())).withRel("telefones"),
				linkTo(methodOn(UsuarioControle.class).listarDocumentos(usuario.getId())).withRel("documentos"),
				linkTo(methodOn(UsuarioControle.class).listarEmails(usuario.getId())).withRel("emails"),
				linkTo(methodOn(UsuarioControle.class).listarCredenciais(usuario.getId())).withRel("credenciais"),
				linkTo(methodOn(UsuarioControle.class).listarVeiculos(usuario.getId())).withRel("veiculos"),
				linkTo(methodOn(UsuarioControle.class).atualizar(usuario.getId(), new UsuarioAtualizacaoDTO())).withRel("atualizar"),
				linkTo(methodOn(UsuarioControle.class).remover(usuario.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<UsuarioRespostaDTO>> toCollectionModel(Long empresaId, List<UsuarioRespostaDTO> usuarios) {
		List<EntityModel<UsuarioRespostaDTO>> modelos = usuarios.stream().map(this::toModel).collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(UsuarioControle.class).listarDaEmpresa(empresaId)).withSelfRel(),
				linkTo(methodOn(UsuarioControle.class).cadastrar(empresaId, new UsuarioCadastroDTO())).withRel("cadastrar"));
	}
}
