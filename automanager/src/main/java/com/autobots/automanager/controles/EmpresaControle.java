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

import com.autobots.automanager.modelo.dto.empresa.EmpresaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaCadastroDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaRespostaDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.montadores.EmpresaModelAssembler;
import com.autobots.automanager.montadores.EmpresaSubrecursoModelAssembler;
import com.autobots.automanager.servicos.empresa.EmpresaServico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@Tag(name = "Empresas", description = "Operações relacionadas a empresas")
public class EmpresaControle {

	@Autowired
	private EmpresaServico servico;

	@Autowired
	private EmpresaModelAssembler assembler;

	@Autowired
	private EmpresaSubrecursoModelAssembler subrecursoAssembler;

	@GetMapping
	@Operation(summary = "Listar todas as empresas", description = "Retorna uma lista com todas as empresas cadastradas")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de empresas obtida com sucesso")
	})
	public ResponseEntity<CollectionModel<EntityModel<EmpresaRespostaDTO>>> listar() {
		return ResponseEntity.ok(assembler.toCollectionModel(servico.listar()));
	}

	@GetMapping("/{empresaId}")
	@Operation(summary = "Buscar empresa por ID", description = "Busca uma empresa específica pelo seu ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso"),
		@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	public ResponseEntity<EntityModel<EmpresaRespostaDTO>> buscarPorId(@PathVariable Long empresaId) {
		return ResponseEntity.ok(assembler.toModel(servico.buscarPorId(empresaId)));
	}

	@PostMapping
	@Operation(summary = "Cadastrar nova empresa", description = "Cria uma nova empresa com os dados fornecidos")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Empresa criada com sucesso"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos")
	})
	public ResponseEntity<EntityModel<EmpresaRespostaDTO>> cadastrar(@Valid @RequestBody EmpresaCadastroDTO dto) {
		EmpresaRespostaDTO criada = servico.cadastrar(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{empresaId}")
				.buildAndExpand(criada.getId()).toUri();
		return ResponseEntity.created(location).body(assembler.toModel(criada));
	}

	@PutMapping("/{empresaId}")
	@Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
		@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	public ResponseEntity<EntityModel<EmpresaRespostaDTO>> atualizar(@PathVariable Long empresaId,
			@Valid @RequestBody EmpresaAtualizacaoDTO dto) {
		return ResponseEntity.ok(assembler.toModel(servico.atualizar(empresaId, dto)));
	}

	@DeleteMapping("/{empresaId}")
	@Operation(summary = "Remover empresa", description = "Rеmоvе umа еmprеsа dо sistеmа")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Empresa removida com sucesso"),
		@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	public ResponseEntity<Void> remover(@PathVariable Long empresaId) {
		servico.remover(empresaId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{empresaId}/endereco")
	public ResponseEntity<EntityModel<EnderecoDTO>> buscarEndereco(@PathVariable Long empresaId) {
		return ResponseEntity.ok(subrecursoAssembler.toEnderecoModel(empresaId, servico.buscarEndereco(empresaId)));
	}

	@PostMapping("/{empresaId}/endereco")
	public ResponseEntity<EntityModel<EnderecoDTO>> cadastrarEndereco(@PathVariable Long empresaId,
			@Valid @RequestBody EnderecoDTO dto) {
		EnderecoDTO endereco = servico.cadastrarEndereco(empresaId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toEnderecoModel(empresaId, endereco));
	}

	@PutMapping("/{empresaId}/endereco")
	public ResponseEntity<EntityModel<EnderecoDTO>> atualizarEndereco(@PathVariable Long empresaId,
			@Valid @RequestBody EnderecoDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toEnderecoModel(empresaId, servico.atualizarEndereco(empresaId, dto)));
	}

	@DeleteMapping("/{empresaId}/endereco")
	public ResponseEntity<Void> removerEndereco(@PathVariable Long empresaId) {
		servico.removerEndereco(empresaId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{empresaId}/telefones")
	public ResponseEntity<CollectionModel<EntityModel<TelefoneDTO>>> listarTelefones(@PathVariable Long empresaId) {
		return ResponseEntity.ok(subrecursoAssembler.toTelefoneCollectionModel(empresaId, servico.listarTelefones(empresaId)));
	}

	@GetMapping("/{empresaId}/telefones/{telefoneId}")
	public ResponseEntity<EntityModel<TelefoneDTO>> buscarTelefone(@PathVariable Long empresaId,
			@PathVariable Long telefoneId) {
		return ResponseEntity.ok(subrecursoAssembler.toTelefoneModel(empresaId, servico.buscarTelefone(empresaId, telefoneId)));
	}

	@PostMapping("/{empresaId}/telefones")
	public ResponseEntity<EntityModel<TelefoneDTO>> cadastrarTelefone(@PathVariable Long empresaId,
			@Valid @RequestBody TelefoneDTO dto) {
		TelefoneDTO telefone = servico.cadastrarTelefone(empresaId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{telefoneId}")
				.buildAndExpand(telefone.getId()).toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toTelefoneModel(empresaId, telefone));
	}

	@PutMapping("/{empresaId}/telefones/{telefoneId}")
	public ResponseEntity<EntityModel<TelefoneDTO>> atualizarTelefone(@PathVariable Long empresaId,
			@PathVariable Long telefoneId, @Valid @RequestBody TelefoneDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toTelefoneModel(empresaId, servico.atualizarTelefone(empresaId, telefoneId, dto)));
	}

	@DeleteMapping("/{empresaId}/telefones/{telefoneId}")
	public ResponseEntity<Void> removerTelefone(@PathVariable Long empresaId, @PathVariable Long telefoneId) {
		servico.removerTelefone(empresaId, telefoneId);
		return ResponseEntity.noContent().build();
	}
}
