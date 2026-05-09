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

import com.autobots.automanager.modelo.dto.servico.ServicoAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoCadastroDTO;
import com.autobots.automanager.modelo.dto.servico.ServicoRespostaDTO;
import com.autobots.automanager.montadores.ServicoPrestadoModelAssembler;
import com.autobots.automanager.servicos.servico.ServicoPrestadoServico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas/{empresaId}/servicos")
@Tag(name = "Serviços", description = "Operações relacionadas a serviços prestados")
public class ServicoPrestadoControle {

	@Autowired
	private ServicoPrestadoServico servico;

	@Autowired
	private ServicoPrestadoModelAssembler assembler;

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<ServicoRespostaDTO>>> listar(@PathVariable Long empresaId) {
		return ResponseEntity.ok(assembler.toCollectionModel(empresaId, servico.listarDaEmpresa(empresaId)));
	}

	@GetMapping("/{servicoId}")
	public ResponseEntity<EntityModel<ServicoRespostaDTO>> buscarPorId(@PathVariable Long empresaId,
			@PathVariable Long servicoId) {
		return ResponseEntity.ok(assembler.toModel(servico.buscarDaEmpresa(empresaId, servicoId)));
	}

	@PostMapping
	public ResponseEntity<EntityModel<ServicoRespostaDTO>> cadastrar(@PathVariable Long empresaId,
			@Valid @RequestBody ServicoCadastroDTO dto) {
		ServicoRespostaDTO servicoResposta = servico.cadastrar(empresaId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{servicoId}")
				.buildAndExpand(servicoResposta.getId()).toUri();
		return ResponseEntity.created(location).body(assembler.toModel(servicoResposta));
	}

	@PutMapping("/{servicoId}")
	public ResponseEntity<EntityModel<ServicoRespostaDTO>> atualizar(@PathVariable Long empresaId,
			@PathVariable Long servicoId, @Valid @RequestBody ServicoAtualizacaoDTO dto) {
		return ResponseEntity.ok(assembler.toModel(servico.atualizar(empresaId, servicoId, dto)));
	}

	@DeleteMapping("/{servicoId}")
	public ResponseEntity<Void> remover(@PathVariable Long empresaId, @PathVariable Long servicoId) {
		servico.remover(empresaId, servicoId);
		return ResponseEntity.noContent().build();
	}
}
