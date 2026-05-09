package com.autobots.automanager.montadores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.controles.ServicoPrestadoControle;
import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.modelo.dto.empresa.EmpresaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaCadastroDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaRespostaDTO;

@Component
public class EmpresaModelAssembler {

	public EntityModel<EmpresaRespostaDTO> toModel(EmpresaRespostaDTO empresa) {
		return EntityModel.of(empresa,
				linkTo(methodOn(EmpresaControle.class).buscarPorId(empresa.getId())).withSelfRel(),
				linkTo(methodOn(EmpresaControle.class).listar()).withRel("empresas"),
				linkTo(methodOn(EmpresaControle.class).buscarEndereco(empresa.getId())).withRel("endereco"),
				linkTo(methodOn(EmpresaControle.class).listarTelefones(empresa.getId())).withRel("telefones"),
				linkTo(methodOn(UsuarioControle.class).listarDaEmpresa(empresa.getId())).withRel("usuarios"),
				linkTo(methodOn(MercadoriaControle.class).listar(empresa.getId())).withRel("mercadorias"),
				linkTo(methodOn(ServicoPrestadoControle.class).listar(empresa.getId())).withRel("servicos"),
				linkTo(methodOn(VendaControle.class).listar(empresa.getId())).withRel("vendas"),
				linkTo(methodOn(EmpresaControle.class).atualizar(empresa.getId(), new EmpresaAtualizacaoDTO())).withRel("atualizar"),
				linkTo(methodOn(EmpresaControle.class).remover(empresa.getId())).withRel("remover"));
	}

	public CollectionModel<EntityModel<EmpresaRespostaDTO>> toCollectionModel(List<EmpresaRespostaDTO> empresas) {
		List<EntityModel<EmpresaRespostaDTO>> modelos = empresas.stream().map(this::toModel).collect(Collectors.toList());
		return CollectionModel.of(modelos,
				linkTo(methodOn(EmpresaControle.class).listar()).withSelfRel(),
				linkTo(methodOn(EmpresaControle.class).cadastrar(new EmpresaCadastroDTO())).withRel("cadastrar"));
	}
}
