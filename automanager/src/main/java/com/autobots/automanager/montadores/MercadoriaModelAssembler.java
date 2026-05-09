package com.autobots.automanager.montadores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaCadastroDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaRespostaDTO;

@Component
public class MercadoriaModelAssembler {

	public EntityModel<MercadoriaRespostaDTO> toModel(MercadoriaRespostaDTO mercadoria) {
		return EntityModel.of(mercadoria,
				linkTo(methodOn(MercadoriaControle.class).buscarPorId(mercadoria.getEmpresaId(), mercadoria.getId())).withSelfRel(),
				linkTo(methodOn(MercadoriaControle.class).listar(mercadoria.getEmpresaId())).withRel("mercadorias"),
				linkTo(methodOn(MercadoriaControle.class).atualizar(mercadoria.getEmpresaId(), mercadoria.getId(), new MercadoriaAtualizacaoDTO())).withRel("atualizar"),
				linkTo(methodOn(MercadoriaControle.class).remover(mercadoria.getEmpresaId(), mercadoria.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<MercadoriaRespostaDTO>> toCollectionModel(Long empresaId, List<MercadoriaRespostaDTO> mercadorias) {
		List<EntityModel<MercadoriaRespostaDTO>> modelos = mercadorias.stream().map(this::toModel).collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(MercadoriaControle.class).listar(empresaId)).withSelfRel(),
				linkTo(methodOn(MercadoriaControle.class).cadastrar(empresaId, new MercadoriaCadastroDTO())).withRel("cadastrar"));
	}
}
