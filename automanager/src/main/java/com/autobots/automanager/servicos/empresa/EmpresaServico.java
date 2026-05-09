package com.autobots.automanager.servicos.empresa;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.atualizadores.empresa.EmpresaAtualizador;
import com.autobots.automanager.modelo.atualizadores.endereco.EnderecoAtualizador;
import com.autobots.automanager.modelo.atualizadores.telefone.TelefoneAtualizador;
import com.autobots.automanager.modelo.dto.empresa.EmpresaAtualizacaoDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaCadastroDTO;
import com.autobots.automanager.modelo.dto.empresa.EmpresaRespostaDTO;
import com.autobots.automanager.modelo.dto.endereco.EnderecoDTO;
import com.autobots.automanager.modelo.dto.telefone.TelefoneDTO;
import com.autobots.automanager.modelo.mapeadores.RespostaMapper;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.servicos.SuporteDominioServico;

@Service
public class EmpresaServico {

	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@Autowired
	private TelefoneRepositorio telefoneRepositorio;

	@Autowired
	private EmpresaAtualizador empresaAtualizador;

	@Autowired
	private EnderecoAtualizador enderecoAtualizador;

	@Autowired
	private TelefoneAtualizador telefoneAtualizador;

	@Autowired
	private RespostaMapper respostaMapper;

	@Autowired
	private SuporteDominioServico suporte;

	public List<EmpresaRespostaDTO> listar() {
		return empresaRepositorio.findAll().stream().map(respostaMapper::paraEmpresa).collect(Collectors.toList());
	}

	public EmpresaRespostaDTO buscarPorId(Long empresaId) {
		return respostaMapper.paraEmpresa(suporte.buscarEmpresa(empresaId));
	}

	@Transactional
	public EmpresaRespostaDTO cadastrar(EmpresaCadastroDTO dto) {
		Empresa empresa = empresaAtualizador.criar(dto);
		if (dto.getEndereco() != null) {
			empresa.setEndereco(enderecoAtualizador.criar(dto.getEndereco()));
		}
		if (dto.getTelefones() != null) {
			for (TelefoneDTO telefoneDto : dto.getTelefones()) {
				Telefone telefone = telefoneAtualizador.criar(telefoneDto);
				telefone.setEmpresa(empresa);
				empresa.getTelefones().add(telefone);
			}
		}
		return respostaMapper.paraEmpresa(empresaRepositorio.save(empresa));
	}

	@Transactional
	public EmpresaRespostaDTO atualizar(Long empresaId, EmpresaAtualizacaoDTO dto) {
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		empresaAtualizador.atualizar(empresa, dto);
		if (dto.getEndereco() != null) {
			if (empresa.getEndereco() == null) {
				empresa.setEndereco(enderecoAtualizador.criar(dto.getEndereco()));
			} else {
				enderecoAtualizador.atualizar(empresa.getEndereco(), dto.getEndereco());
			}
		}
		return respostaMapper.paraEmpresa(empresaRepositorio.save(empresa));
	}

	public void remover(Long empresaId) {
		empresaRepositorio.delete(suporte.buscarEmpresa(empresaId));
	}

	public EnderecoDTO buscarEndereco(Long empresaId) {
		Endereco endereco = suporte.buscarEmpresa(empresaId).getEndereco();
		if (endereco == null) {
			throw new com.autobots.automanager.excecoes.RecursoNaoEncontradoException("Endereço não cadastrado para a empresa");
		}
		return respostaMapper.paraEndereco(endereco);
	}

	@Transactional
	public EnderecoDTO cadastrarEndereco(Long empresaId, EnderecoDTO dto) {
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		if (empresa.getEndereco() != null) {
			throw new com.autobots.automanager.excecoes.ConflitoDeRecursoException("Empresa já possui endereço cadastrado");
		}
		empresa.setEndereco(enderecoAtualizador.criar(dto));
		empresaRepositorio.save(empresa);
		return respostaMapper.paraEndereco(empresa.getEndereco());
	}

	@Transactional
	public EnderecoDTO atualizarEndereco(Long empresaId, EnderecoDTO dto) {
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		if (empresa.getEndereco() == null) {
			empresa.setEndereco(enderecoAtualizador.criar(dto));
		} else {
			enderecoAtualizador.atualizar(empresa.getEndereco(), dto);
		}
		empresaRepositorio.save(empresa);
		return respostaMapper.paraEndereco(empresa.getEndereco());
	}

	@Transactional
	public void removerEndereco(Long empresaId) {
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		if (empresa.getEndereco() == null) {
			throw new com.autobots.automanager.excecoes.RecursoNaoEncontradoException("Endereço não cadastrado para a empresa");
		}
		empresa.setEndereco(null);
		empresaRepositorio.save(empresa);
	}

	public List<TelefoneDTO> listarTelefones(Long empresaId) {
		return respostaMapper.paraTelefones(suporte.buscarEmpresa(empresaId).getTelefones());
	}

	public TelefoneDTO buscarTelefone(Long empresaId, Long telefoneId) {
		Telefone telefone = suporte.buscarTelefone(telefoneId);
		suporte.validarTelefoneDaEmpresa(empresaId, telefone);
		return respostaMapper.paraTelefone(telefone);
	}

	@Transactional
	public TelefoneDTO cadastrarTelefone(Long empresaId, TelefoneDTO dto) {
		Empresa empresa = suporte.buscarEmpresa(empresaId);
		Telefone telefone = telefoneAtualizador.criar(dto);
		telefone.setEmpresa(empresa);
		return respostaMapper.paraTelefone(telefoneRepositorio.save(telefone));
	}

	@Transactional
	public TelefoneDTO atualizarTelefone(Long empresaId, Long telefoneId, TelefoneDTO dto) {
		Telefone telefone = suporte.buscarTelefone(telefoneId);
		suporte.validarTelefoneDaEmpresa(empresaId, telefone);
		telefoneAtualizador.atualizar(telefone, dto);
		return respostaMapper.paraTelefone(telefoneRepositorio.save(telefone));
	}

	public void removerTelefone(Long empresaId, Long telefoneId) {
		Telefone telefone = suporte.buscarTelefone(telefoneId);
		suporte.validarTelefoneDaEmpresa(empresaId, telefone);
		telefoneRepositorio.delete(telefone);
	}
}
