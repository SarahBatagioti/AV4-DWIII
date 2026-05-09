package com.autobots.automanager.montadores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;

@Component
public class EmpresaSubrecursoModelAssembler {

	public EntityModel<EnderecoDTO> toEnderecoModel(Long empresaId, EnderecoDTO endereco) {
		return EntityModel.of(endereco,
				linkTo(methodOn(EmpresaControle.class).buscarEndereco(empresaId)).withSelfRel(),
				linkTo(methodOn(EmpresaControle.class).buscarPorId(empresaId)).withRel("empresa"),
				linkTo(methodOn(EmpresaControle.class).atualizarEndereco(empresaId, new EnderecoDTO())).withRel("atualizar"),
				linkTo(methodOn(EmpresaControle.class).removerEndereco(empresaId)).withRel("remover"));
	}

	public EntityModel<TelefoneDTO> toTelefoneModel(Long empresaId, TelefoneDTO telefone) {
		return EntityModel.of(telefone,
				linkTo(methodOn(EmpresaControle.class).buscarTelefone(empresaId, telefone.getId())).withSelfRel(),
				linkTo(methodOn(EmpresaControle.class).listarTelefones(empresaId)).withRel("telefones"),
				linkTo(methodOn(EmpresaControle.class).buscarPorId(empresaId)).withRel("empresa"),
				linkTo(methodOn(EmpresaControle.class).atualizarTelefone(empresaId, telefone.getId(), new TelefoneDTO())).withRel("atualizar"),
				linkTo(methodOn(EmpresaControle.class).removerTelefone(empresaId, telefone.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<TelefoneDTO>> toTelefoneCollectionModel(Long empresaId, List<TelefoneDTO> telefones) {
		List<EntityModel<TelefoneDTO>> modelos = telefones.stream().map(dto -> toTelefoneModel(empresaId, dto))
				.collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(EmpresaControle.class).listarTelefones(empresaId)).withSelfRel(),
				linkTo(methodOn(EmpresaControle.class).buscarPorId(empresaId)).withRel("empresa"),
				linkTo(methodOn(EmpresaControle.class).cadastrarTelefone(empresaId, new TelefoneDTO())).withRel("cadastrar"));
	}
}
