package com.autobots.automanager.montadores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoPrestadoControle;
import com.autobots.automanager.modelo.dto.servico.ServicoAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoCadastroDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoRespostaDTO;

@Component
public class ServicoPrestadoModelAssembler {

	public EntityModel<ServicoRespostaDTO> toModel(ServicoRespostaDTO servico) {
		return EntityModel.of(servico,
				linkTo(methodOn(ServicoPrestadoControle.class).buscarPorId(servico.getEmpresaId(), servico.getId())).withSelfRel(),
				linkTo(methodOn(ServicoPrestadoControle.class).listar(servico.getEmpresaId())).withRel("servicos"),
				linkTo(methodOn(ServicoPrestadoControle.class).atualizar(servico.getEmpresaId(), servico.getId(), new ServicoAtualizacaoDTO())).withRel("atualizar"),
				linkTo(methodOn(ServicoPrestadoControle.class).remover(servico.getEmpresaId(), servico.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<ServicoRespostaDTO>> toCollectionModel(Long empresaId, List<ServicoRespostaDTO> servicos) {
		List<EntityModel<ServicoRespostaDTO>> modelos = servicos.stream().map(this::toModel).collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(ServicoPrestadoControle.class).listar(empresaId)).withSelfRel(),
				linkTo(methodOn(ServicoPrestadoControle.class).cadastrar(empresaId, new ServicoCadastroDTO())).withRel("cadastrar"));
	}
}
