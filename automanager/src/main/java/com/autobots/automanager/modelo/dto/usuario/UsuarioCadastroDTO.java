package com.autobots.automanager.modelo.dto.usuario;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.modelo.dto.credencial.CredencialDTO;
import com.autobots.automanager.modelo.dto.documento.DocumentoDTO;
import com.autobots.automanager.modelo.dto.email.EmailDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.modelo.dto.veiculo.VeiculoDTO;

import lombok.Data;

@Data
public class UsuarioCadastroDTO {
	@NotBlank(message = "nome deve ser informado")
	private String nome;
	private String nomeSocial;
	private Date dataNascimento;
	@NotEmpty(message = "ao menos um perfil deve ser informado")
	private Set<PerfilUsuario> perfis;
	@Valid
	private EnderecoDTO endereco;
	@Valid
	private List<TelefoneDTO> telefones;
	@Valid
	private List<DocumentoDTO> documentos;
	@Valid
	private List<EmailDTO> emails;
	@Valid
	private List<CredencialDTO> credenciais;
	@Valid
	private List<VeiculoDTO> veiculos;
}
