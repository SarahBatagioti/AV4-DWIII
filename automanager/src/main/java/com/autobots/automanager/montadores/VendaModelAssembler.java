package com.autobots.automanager.montadores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.modelo.dto.venda.VendaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.venda.VendaCadastroDTO;
import com.autobots.automanager.modelo.dto.venda.VendaRespostaDTO;

@Component
public class VendaModelAssembler {

	public EntityModel<VendaRespostaDTO> toModel(VendaRespostaDTO venda) {
		return EntityModel.of(venda,
				linkTo(methodOn(VendaControle.class).buscarPorId(venda.getEmpresaId(), venda.getId())).withSelfRel(),
				linkTo(methodOn(VendaControle.class).listar(venda.getEmpresaId())).withRel("vendas"),
				linkTo(methodOn(VendaControle.class).atualizar(venda.getEmpresaId(), venda.getId(), new VendaAtualizacaoDTO())).withRel("atualizar"),
				linkTo(methodOn(VendaControle.class).remover(venda.getEmpresaId(), venda.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<VendaRespostaDTO>> toCollectionModel(Long empresaId, List<VendaRespostaDTO> vendas) {
		List<EntityModel<VendaRespostaDTO>> modelos = vendas.stream().map(this::toModel).collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(VendaControle.class).listar(empresaId)).withSelfRel(),
				linkTo(methodOn(VendaControle.class).cadastrar(empresaId, new VendaCadastroDTO())).withRel("cadastrar"));
	}
}
