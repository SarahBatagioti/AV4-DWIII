package com.autobots.automanager.seguranca;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("ci")
@Transactional
class SegurancaIntegracaoTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private MercadoriaRepositorio mercadoriaRepositorio;

	@Autowired
	private ServicoRepositorio servicoRepositorio;

	@Autowired
	private VendaRepositorio vendaRepositorio;

	private Empresa empresa;
	private Empresa outraEmpresa;
	private Usuario admin;
	private Usuario gerente;
	private Usuario vendedor;
	private Usuario cliente;
	private Usuario outroCliente;
	private Usuario clienteOutraEmpresa;
	private Venda vendaDoVendedor;
	private Venda vendaDeOutroCliente;

	@BeforeEach
	void setUp() {
		empresa = salvarEmpresa("AutoBots Matriz");
		outraEmpresa = salvarEmpresa("AutoBots Filial");
		admin = salvarUsuario(empresa, "Administrador", "admin", "senha123", false, PerfilUsuario.ADMINISTRADOR);
		gerente = salvarUsuario(empresa, "Gerente", "gerente", "senha123", false, PerfilUsuario.GERENTE);
		vendedor = salvarUsuario(empresa, "Vendedor", "vendedor", "senha123", false, PerfilUsuario.VENDEDOR);
		cliente = salvarUsuario(empresa, "Cliente", "cliente", "senha123", false, PerfilUsuario.CLIENTE);
		outroCliente = salvarUsuario(empresa, "Outro Cliente", "outro-cliente", "senha123", false, PerfilUsuario.CLIENTE);
		clienteOutraEmpresa = salvarUsuario(outraEmpresa, "Cliente Filial", "cliente-filial", "senha123", false,
				PerfilUsuario.CLIENTE);

		Mercadoria mercadoria = new Mercadoria();
		mercadoria.setEmpresa(empresa);
		mercadoria.setNome("Óleo 5W30");
		mercadoria.setCadastro(new Date());
		mercadoria.setFabricao(new Date());
		mercadoria.setValidade(new Date(System.currentTimeMillis() + 86400000L));
		mercadoria.setQuantidade(10L);
		mercadoria.setValor(59.9);
		mercadoriaRepositorio.save(mercadoria);

		Servico servico = new Servico();
		servico.setEmpresa(empresa);
		servico.setNome("Troca de óleo");
		servico.setValor(120.0);
		servicoRepositorio.save(servico);

		vendaDoVendedor = salvarVenda("VENDA-001", empresa, cliente, vendedor);
		vendaDeOutroCliente = salvarVenda("VENDA-002", empresa, outroCliente, vendedor);
	}

	@Test
	void deveAutenticarComCredencialValidaERetornarBearerToken() throws Exception {
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("nomeUsuario", "admin", "senha", "senha123"))))
				.andExpect(status().isOk())
				.andExpect(header().string("Authorization", org.hamcrest.Matchers.startsWith("Bearer ")));
	}

	@Test
	void deveRecusarLoginComCredencialInativa() throws Exception {
		salvarUsuario(empresa, "Inativo", "inativo", "senha123", true, PerfilUsuario.CLIENTE);

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("nomeUsuario", "inativo", "senha", "senha123"))))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void deveExigirTokenParaAcessarRotaProtegida() throws Exception {
		mockMvc.perform(get("/empresas")).andExpect(status().isUnauthorized());
	}

	@Test
	void administradorDeveListarEmpresas() throws Exception {
		mockMvc.perform(get("/empresas").header("Authorization", token("admin", "senha123")))
				.andExpect(status().isOk());
	}

	@Test
	void gerenteDeveCadastrarClienteMasNaoAdministrador() throws Exception {
		Map<String, Object> payloadCliente = Map.of("nome", "Cliente Novo", "perfis", new String[] { "CLIENTE" });
		Map<String, Object> payloadAdmin = Map.of("nome", "Admin Novo", "perfis", new String[] { "ADMINISTRADOR" });

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/usuarios")
				.header("Authorization", token("gerente", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payloadCliente))).andExpect(status().isCreated());

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/usuarios")
				.header("Authorization", token("gerente", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payloadAdmin))).andExpect(status().isForbidden());
	}

	@Test
	void gerenteNaoDeveAlterarAdministrador() throws Exception {
		mockMvc.perform(put("/usuarios/" + admin.getId()).header("Authorization", token("gerente", "senha123"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("nome", "Admin Alterado"))))
				.andExpect(status().isForbidden());
	}

	@Test
	void gerenteDeveGerenciarMercadoriasEServicos() throws Exception {
		Map<String, Object> mercadoriaPayload = Map.of("nome", "Filtro de ar", "quantidade", 5, "valor", 39.9,
				"fabricao", new Date().getTime(), "validade", new Date(System.currentTimeMillis() + 86400000L).getTime());
		Map<String, Object> servicoPayload = Map.of("nome", "Alinhamento", "valor", 150.0);

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/mercadorias")
				.header("Authorization", token("gerente", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mercadoriaPayload))).andExpect(status().isCreated());

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/servicos")
				.header("Authorization", token("gerente", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(servicoPayload))).andExpect(status().isCreated());
	}

	@Test
	void gerenteDeveCadastrarMercadoriaComDatasIso8601() throws Exception {
		String payload = """
				{
				  "nome": "Filtro de cabine",
				  "quantidade": 3,
				  "valor": 49.9,
				  "fabricao": "2026-05-23T10:15:30Z",
				  "validade": "2027-05-23"
				}
				""";

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/mercadorias")
				.header("Authorization", token("gerente", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(payload)).andExpect(status().isCreated());
	}

	@Test
	void vendedorDeveLerMercadoriasMasNaoCadastrar() throws Exception {
		Map<String, Object> payload = Map.of("nome", "Pastilha", "quantidade", 2, "valor", 89.9, "fabricao",
				new Date().getTime(), "validade", new Date(System.currentTimeMillis() + 86400000L).getTime());

		mockMvc.perform(get("/empresas/" + empresa.getId() + "/mercadorias")
				.header("Authorization", token("vendedor", "senha123"))).andExpect(status().isOk());

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/mercadorias")
				.header("Authorization", token("vendedor", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload))).andExpect(status().isForbidden());
	}

	@Test
	void vendedorDeveCriarSomenteVendaPropria() throws Exception {
		Map<String, Object> vendaPropria = Map.of("identificacao", "VENDA-010", "clienteId", cliente.getId(),
				"funcionarioId", vendedor.getId());
		Map<String, Object> vendaDeTerceiro = Map.of("identificacao", "VENDA-011", "clienteId", cliente.getId(),
				"funcionarioId", gerente.getId());

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/vendas")
				.header("Authorization", token("vendedor", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(vendaPropria))).andExpect(status().isCreated());

		mockMvc.perform(post("/empresas/" + empresa.getId() + "/vendas")
				.header("Authorization", token("vendedor", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(vendaDeTerceiro))).andExpect(status().isForbidden());
	}

	@Test
	void vendedorNaoDeveAtualizarVenda() throws Exception {
		mockMvc.perform(put("/empresas/" + empresa.getId() + "/vendas/" + vendaDoVendedor.getId())
				.header("Authorization", token("vendedor", "senha123")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("identificacao", "VENDA-999"))))
				.andExpect(status().isForbidden());
	}

	@Test
	void clienteDeveLerSomenteProprioCadastro() throws Exception {
		mockMvc.perform(get("/usuarios/" + cliente.getId()).header("Authorization", token("cliente", "senha123")))
				.andExpect(status().isOk());

		mockMvc.perform(get("/usuarios/" + outroCliente.getId()).header("Authorization", token("cliente", "senha123")))
				.andExpect(status().isForbidden());
	}

	@Test
	void clienteDeveLerSomenteAsPropriasVendas() throws Exception {
		mockMvc.perform(get("/empresas/" + empresa.getId() + "/vendas/" + vendaDoVendedor.getId())
				.header("Authorization", token("cliente", "senha123"))).andExpect(status().isOk());

		mockMvc.perform(get("/empresas/" + empresa.getId() + "/vendas/" + vendaDeOutroCliente.getId())
				.header("Authorization", token("cliente", "senha123"))).andExpect(status().isForbidden());
	}

	@Test
	void clienteNaoDeveAcessarUsuariosDeOutraEmpresaMesmoComMesmoPerfil() throws Exception {
		mockMvc.perform(get("/usuarios/" + clienteOutraEmpresa.getId())
				.header("Authorization", token("cliente", "senha123"))).andExpect(status().isForbidden());
	}

	private String token(String nomeUsuario, String senha) throws Exception {
		return mockMvc
				.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(Map.of("nomeUsuario", nomeUsuario, "senha", senha))))
				.andReturn().getResponse().getHeader("Authorization");
	}

	private Empresa salvarEmpresa(String razaoSocial) {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial(razaoSocial);
		empresa.setCadastro(new Date());
		return empresaRepositorio.save(empresa);
	}

	private Usuario salvarUsuario(Empresa empresa, String nome, String nomeUsuario, String senha, boolean inativo,
			PerfilUsuario... perfis) {
		Usuario usuario = new Usuario();
		usuario.setEmpresa(empresa);
		usuario.setNome(nome);
		usuario.setDataCadastro(new Date());
		Set<PerfilUsuario> perfisUsuario = new LinkedHashSet<>();
		for (PerfilUsuario perfil : perfis) {
			perfisUsuario.add(perfil);
		}
		usuario.setPerfis(perfisUsuario);

		CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
		credencial.setCriacao(new Date());
		credencial.setInativo(inativo);
		credencial.setNomeUsuario(nomeUsuario);
		credencial.setSenha(passwordEncoder.encode(senha));
		credencial.setUsuario(usuario);
		usuario.getCredenciais().add(credencial);

		return usuarioRepositorio.save(usuario);
	}

	private Venda salvarVenda(String identificacao, Empresa empresa, Usuario clienteVenda, Usuario funcionario) {
		Venda venda = new Venda();
		venda.setCadastro(new Date());
		venda.setIdentificacao(identificacao);
		venda.setEmpresa(empresa);
		venda.setCliente(clienteVenda);
		venda.setFuncionario(funcionario);
		return vendaRepositorio.save(venda);
	}
}
