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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.modelo.dto.credencial.CredencialDTO;
import com.autobots.automanager.modelo.dto.documento.DocumentoDTO;
import com.autobots.automanager.modelo.dto.email.EmailDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioCadastroDTO;
import com.autobots.automanager.modelo.dto.usuario.UsuarioRespostaDTO;
import com.autobots.automanager.modelo.dto.veiculo.VeiculoDTO;
import com.autobots.automanager.montadores.UsuarioModelAssembler;
import com.autobots.automanager.montadores.UsuarioSubrecursoModelAssembler;
import com.autobots.automanager.servicos.usuario.UsuarioServico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UsuarioControle {

	@Autowired
	private UsuarioServico servico;

	@Autowired
	private UsuarioModelAssembler assembler;

	@Autowired
	private UsuarioSubrecursoModelAssembler subrecursoAssembler;

	@GetMapping("/empresas/{empresaId}/usuarios")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	@Operation(summary = "Listar usuários de uma empresa", description = "Retorna todos os usuários associados a uma empresa específica")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de usuários obtida com sucesso"),
		@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	public ResponseEntity<CollectionModel<EntityModel<UsuarioRespostaDTO>>> listarDaEmpresa(@PathVariable Long empresaId) {
		return ResponseEntity.ok(assembler.toCollectionModel(empresaId, servico.listarDaEmpresa(empresaId)));
	}

	@PostMapping("/empresas/{empresaId}/usuarios")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<UsuarioRespostaDTO>> cadastrar(@PathVariable Long empresaId,
			@Valid @RequestBody UsuarioCadastroDTO dto) {
		UsuarioRespostaDTO usuario = servico.cadastrar(empresaId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/usuarios/{usuarioId}")
				.buildAndExpand(usuario.getId()).toUri();
		return ResponseEntity.created(location).body(assembler.toModel(usuario));
	}

	@GetMapping("/usuarios/{usuarioId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<EntityModel<UsuarioRespostaDTO>> buscarPorId(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(assembler.toModel(servico.buscarPorId(usuarioId)));
	}

	@PutMapping("/usuarios/{usuarioId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<UsuarioRespostaDTO>> atualizar(@PathVariable Long usuarioId,
			@Valid @RequestBody UsuarioAtualizacaoDTO dto) {
		return ResponseEntity.ok(assembler.toModel(servico.atualizar(usuarioId, dto)));
	}

	@DeleteMapping("/usuarios/{usuarioId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<Void> remover(@PathVariable Long usuarioId) {
		servico.remover(usuarioId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuarios/{usuarioId}/endereco")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<EntityModel<EnderecoDTO>> buscarEndereco(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(subrecursoAssembler.toEnderecoModel(usuarioId, servico.buscarEndereco(usuarioId)));
	}

	@PostMapping("/usuarios/{usuarioId}/endereco")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<EnderecoDTO>> cadastrarEndereco(@PathVariable Long usuarioId,
			@Valid @RequestBody EnderecoDTO dto) {
		EnderecoDTO endereco = servico.cadastrarEndereco(usuarioId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toEnderecoModel(usuarioId, endereco));
	}

	@PutMapping("/usuarios/{usuarioId}/endereco")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<EnderecoDTO>> atualizarEndereco(@PathVariable Long usuarioId,
			@Valid @RequestBody EnderecoDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toEnderecoModel(usuarioId, servico.atualizarEndereco(usuarioId, dto)));
	}

	@DeleteMapping("/usuarios/{usuarioId}/endereco")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<Void> removerEndereco(@PathVariable Long usuarioId) {
		servico.removerEndereco(usuarioId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuarios/{usuarioId}/telefones")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<CollectionModel<EntityModel<TelefoneDTO>>> listarTelefones(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(subrecursoAssembler.toTelefoneCollectionModel(usuarioId, servico.listarTelefones(usuarioId)));
	}

	@GetMapping("/usuarios/{usuarioId}/telefones/{telefoneId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<EntityModel<TelefoneDTO>> buscarTelefone(@PathVariable Long usuarioId,
			@PathVariable Long telefoneId) {
		return ResponseEntity.ok(subrecursoAssembler.toTelefoneModel(usuarioId, servico.buscarTelefone(usuarioId, telefoneId)));
	}

	@PostMapping("/usuarios/{usuarioId}/telefones")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<TelefoneDTO>> cadastrarTelefone(@PathVariable Long usuarioId,
			@Valid @RequestBody TelefoneDTO dto) {
		TelefoneDTO telefone = servico.cadastrarTelefone(usuarioId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{telefoneId}")
				.buildAndExpand(telefone.getId()).toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toTelefoneModel(usuarioId, telefone));
	}

	@PutMapping("/usuarios/{usuarioId}/telefones/{telefoneId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<TelefoneDTO>> atualizarTelefone(@PathVariable Long usuarioId,
			@PathVariable Long telefoneId, @Valid @RequestBody TelefoneDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toTelefoneModel(usuarioId, servico.atualizarTelefone(usuarioId, telefoneId, dto)));
	}

	@DeleteMapping("/usuarios/{usuarioId}/telefones/{telefoneId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<Void> removerTelefone(@PathVariable Long usuarioId, @PathVariable Long telefoneId) {
		servico.removerTelefone(usuarioId, telefoneId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuarios/{usuarioId}/documentos")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<CollectionModel<EntityModel<DocumentoDTO>>> listarDocumentos(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(subrecursoAssembler.toDocumentoCollectionModel(usuarioId, servico.listarDocumentos(usuarioId)));
	}

	@GetMapping("/usuarios/{usuarioId}/documentos/{documentoId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<EntityModel<DocumentoDTO>> buscarDocumento(@PathVariable Long usuarioId,
			@PathVariable Long documentoId) {
		return ResponseEntity.ok(subrecursoAssembler.toDocumentoModel(usuarioId, servico.buscarDocumento(usuarioId, documentoId)));
	}

	@PostMapping("/usuarios/{usuarioId}/documentos")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<DocumentoDTO>> cadastrarDocumento(@PathVariable Long usuarioId,
			@Valid @RequestBody DocumentoDTO dto) {
		DocumentoDTO documento = servico.cadastrarDocumento(usuarioId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{documentoId}")
				.buildAndExpand(documento.getId()).toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toDocumentoModel(usuarioId, documento));
	}

	@PutMapping("/usuarios/{usuarioId}/documentos/{documentoId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<DocumentoDTO>> atualizarDocumento(@PathVariable Long usuarioId,
			@PathVariable Long documentoId, @Valid @RequestBody DocumentoDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toDocumentoModel(usuarioId, servico.atualizarDocumento(usuarioId, documentoId, dto)));
	}

	@DeleteMapping("/usuarios/{usuarioId}/documentos/{documentoId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<Void> removerDocumento(@PathVariable Long usuarioId, @PathVariable Long documentoId) {
		servico.removerDocumento(usuarioId, documentoId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuarios/{usuarioId}/emails")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<CollectionModel<EntityModel<EmailDTO>>> listarEmails(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(subrecursoAssembler.toEmailCollectionModel(usuarioId, servico.listarEmails(usuarioId)));
	}

	@GetMapping("/usuarios/{usuarioId}/emails/{emailId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<EntityModel<EmailDTO>> buscarEmail(@PathVariable Long usuarioId, @PathVariable Long emailId) {
		return ResponseEntity.ok(subrecursoAssembler.toEmailModel(usuarioId, servico.buscarEmail(usuarioId, emailId)));
	}

	@PostMapping("/usuarios/{usuarioId}/emails")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<EmailDTO>> cadastrarEmail(@PathVariable Long usuarioId,
			@Valid @RequestBody EmailDTO dto) {
		EmailDTO email = servico.cadastrarEmail(usuarioId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{emailId}")
				.buildAndExpand(email.getId()).toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toEmailModel(usuarioId, email));
	}

	@PutMapping("/usuarios/{usuarioId}/emails/{emailId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<EmailDTO>> atualizarEmail(@PathVariable Long usuarioId,
			@PathVariable Long emailId, @Valid @RequestBody EmailDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toEmailModel(usuarioId, servico.atualizarEmail(usuarioId, emailId, dto)));
	}

	@DeleteMapping("/usuarios/{usuarioId}/emails/{emailId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<Void> removerEmail(@PathVariable Long usuarioId, @PathVariable Long emailId) {
		servico.removerEmail(usuarioId, emailId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuarios/{usuarioId}/credenciais")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<CollectionModel<EntityModel<CredencialDTO>>> listarCredenciais(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(subrecursoAssembler.toCredencialCollectionModel(usuarioId, servico.listarCredenciais(usuarioId)));
	}

	@GetMapping("/usuarios/{usuarioId}/credenciais/{credencialId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<EntityModel<CredencialDTO>> buscarCredencial(@PathVariable Long usuarioId,
			@PathVariable Long credencialId) {
		return ResponseEntity.ok(subrecursoAssembler.toCredencialModel(usuarioId, servico.buscarCredencial(usuarioId, credencialId)));
	}

	@PostMapping("/usuarios/{usuarioId}/credenciais")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<CredencialDTO>> cadastrarCredencial(@PathVariable Long usuarioId,
			@Valid @RequestBody CredencialDTO dto) {
		CredencialDTO credencial = servico.cadastrarCredencial(usuarioId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{credencialId}")
				.buildAndExpand(credencial.getId()).toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toCredencialModel(usuarioId, credencial));
	}

	@PutMapping("/usuarios/{usuarioId}/credenciais/{credencialId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<CredencialDTO>> atualizarCredencial(@PathVariable Long usuarioId,
			@PathVariable Long credencialId, @Valid @RequestBody CredencialDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toCredencialModel(usuarioId, servico.atualizarCredencial(usuarioId, credencialId, dto)));
	}

	@DeleteMapping("/usuarios/{usuarioId}/credenciais/{credencialId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<Void> removerCredencial(@PathVariable Long usuarioId, @PathVariable Long credencialId) {
		servico.removerCredencial(usuarioId, credencialId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuarios/{usuarioId}/veiculos")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<CollectionModel<EntityModel<VeiculoDTO>>> listarVeiculos(@PathVariable Long usuarioId) {
		return ResponseEntity.ok(subrecursoAssembler.toVeiculoCollectionModel(usuarioId, servico.listarVeiculos(usuarioId)));
	}

	@GetMapping("/usuarios/{usuarioId}/veiculos/{veiculoId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR','CLIENTE')")
	public ResponseEntity<EntityModel<VeiculoDTO>> buscarVeiculo(@PathVariable Long usuarioId, @PathVariable Long veiculoId) {
		return ResponseEntity.ok(subrecursoAssembler.toVeiculoModel(usuarioId, servico.buscarVeiculo(usuarioId, veiculoId)));
	}

	@PostMapping("/usuarios/{usuarioId}/veiculos")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<VeiculoDTO>> cadastrarVeiculo(@PathVariable Long usuarioId,
			@Valid @RequestBody VeiculoDTO dto) {
		VeiculoDTO veiculo = servico.cadastrarVeiculo(usuarioId, dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{veiculoId}")
				.buildAndExpand(veiculo.getId()).toUri();
		return ResponseEntity.created(location).body(subrecursoAssembler.toVeiculoModel(usuarioId, veiculo));
	}

	@PutMapping("/usuarios/{usuarioId}/veiculos/{veiculoId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<EntityModel<VeiculoDTO>> atualizarVeiculo(@PathVariable Long usuarioId,
			@PathVariable Long veiculoId, @Valid @RequestBody VeiculoDTO dto) {
		return ResponseEntity.ok(subrecursoAssembler.toVeiculoModel(usuarioId, servico.atualizarVeiculo(usuarioId, veiculoId, dto)));
	}

	@DeleteMapping("/usuarios/{usuarioId}/veiculos/{veiculoId}")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','VENDEDOR')")
	public ResponseEntity<Void> removerVeiculo(@PathVariable Long usuarioId, @PathVariable Long veiculoId) {
		servico.removerVeiculo(usuarioId, veiculoId);
		return ResponseEntity.noContent().build();
	}
}
