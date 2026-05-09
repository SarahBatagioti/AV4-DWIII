package com.autobots.automanager.controles;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.modelo.dto.venda.VendaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.venda.VendaCadastroDTO;
import com.autobots.automanager.modelo.dto.venda.VendaRespostaDTO;
import com.autobots.automanager.montadores.VendaModelAssembler;
import com.autobots.automanager.servicos.venda.VendaServico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas/{empresaId}/vendas")
@Tag(name = "Vendas", description = "Operações relacionadas a vendas")
public class VendaControle {

	@Autowired
	private VendaServico servico;

	@Autowired
	private VendaModelAssembler assembler;

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<VendaRespostaDTO>>> listar(@PathVariable Long empresaId) {
		return ResponseEntity.ok(assembler.toCollectionModel(empresaId, servico.listarDaEmpresa(empresaId)));
	}

	@GetMapping("/{vendaId}")
	public ResponseEntity<EntityModel<VendaRespostaDTO>> buscarPorId(@PathVariable Long empresaId,
			@PathVariable Long vendaId) {
		return ResponseEntity.ok(assembler.toModel(servico.buscarDaEmpresa(empresaId, vendaId)));
	}

	@PostMapping
	public ResponseEntity<EntityModel<VendaRespostaDTO>> cadastrar(@PathVariable Long empresaId,
			@Valid @RequestBody VendaCadastroDTO dto) {
		VendaRespostaDTO venda = servico.cadastrar(empresaId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{vendaId}")
				.buildAndExpand(venda.getId()).toUri();
		return ResponseEntity.created(location).body(assembler.toModel(venda));
	}

	@PutMapping("/{vendaId}")
	public ResponseEntity<EntityModel<VendaRespostaDTO>> atualizar(@PathVariable Long empresaId,
			@PathVariable Long vendaId, @Valid @RequestBody VendaAtualizacaoDTO dto) {
		return ResponseEntity.ok(assembler.toModel(servico.atualizar(empresaId, vendaId, dto)));
	}

	@DeleteMapping("/{vendaId}")
	public ResponseEntity<Void> remover(@PathVariable Long empresaId, @PathVariable Long vendaId) {
		servico.remover(empresaId, vendaId);
		return ResponseEntity.noContent().build();
	}
}
