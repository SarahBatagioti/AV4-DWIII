package com.autobots.automanager.controles;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaCadastroDTO;
import com.autobots.automanager.modelo.dto.mercadoria.MercadoriaRespostaDTO;
import com.autobots.automanager.montadores.MercadoriaModelAssembler;
import com.autobots.automanager.servicos.mercadoria.MercadoriaServico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas/{empresaId}/mercadorias")
@Tag(name = "Mercadorias", description = "Operações relacionadas a mercadorias")
public class MercadoriaControle {

	@Autowired
	private MercadoriaServico servico;

	@Autowired
	private MercadoriaModelAssembler assembler;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<CollectionModel<EntityModel<MercadoriaRespostaDTO>>> listar(@PathVariable Long empresaId) {
		return ResponseEntity.ok(assembler.toCollectionModel(empresaId, servico.listarDaEmpresa(empresaId)));
	}

	@GetMapping("/{mercadoriaId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<MercadoriaRespostaDTO>> buscarPorId(@PathVariable Long empresaId,
			@PathVariable Long mercadoriaId) {
		return ResponseEntity.ok(assembler.toModel(servico.buscarDaEmpresa(empresaId, mercadoriaId)));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
	public ResponseEntity<EntityModel<MercadoriaRespostaDTO>> cadastrar(@PathVariable Long empresaId,
			@Valid @RequestBody MercadoriaCadastroDTO dto) {
		MercadoriaRespostaDTO mercadoria = servico.cadastrar(empresaId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{mercadoriaId}")
				.buildAndExpand(mercadoria.getId()).toUri();
		return ResponseEntity.created(location).body(assembler.toModel(mercadoria));
	}

	@PutMapping("/{mercadoriaId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
	public ResponseEntity<EntityModel<MercadoriaRespostaDTO>> atualizar(@PathVariable Long empresaId,
			@PathVariable Long mercadoriaId, @Valid @RequestBody MercadoriaAtualizacaoDTO dto) {
		return ResponseEntity.ok(assembler.toModel(servico.atualizar(empresaId, mercadoriaId, dto)));
	}

	@DeleteMapping("/{mercadoriaId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
	public ResponseEntity<Void> remover(@PathVariable Long empresaId, @PathVariable Long mercadoriaId) {
		servico.remover(empresaId, mercadoriaId);
		return ResponseEntity.noContent().build();
	}
}
