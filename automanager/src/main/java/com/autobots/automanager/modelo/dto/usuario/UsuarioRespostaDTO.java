package com.autobots.automanager.modelo.dto.usuario;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.modelo.dto.credencial.CredencialDTO;
import com.autobots.automanager.modelo.dto.documento.DocumentoDTO;
import com.autobots.automanager.modelo.dto.email.EmailDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.modelo.dto.veiculo.VeiculoDTO;

import lombok.Data;

@Data
public class UsuarioRespostaDTO {
	private Long id;
	private Long empresaId;
	private String nome;
	private String nomeSocial;
	private Date dataNascimento;
	private Date dataCadastro;
	private Set<PerfilUsuario> perfis;
	private EnderecoDTO endereco;
	private List<TelefoneDTO> telefones;
	private List<DocumentoDTO> documentos;
	private List<EmailDTO> emails;
	private List<CredencialDTO> credenciais;
	private List<VeiculoDTO> veiculos;
}
