package com.autobots.automanager.servicos.venda;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.excecoes.RequisicaoInvalidaException;
import com.autobots.automanager.modelo.dto.venda.VendaAtualizacaoDTO;
import com.autobots.automanager.modelo.mapeadores.RespostaMapper;
import com.autobots.automanager.repositorios.VendaRepositorio;
import com.autobots.automanager.servicos.SuporteDominioServico;

@ExtendWith(MockitoExtension.class)
class VendaServicoTest {

	@Mock
	private VendaRepositorio vendaRepositorio;

	@Mock
	private RespostaMapper respostaMapper;

	@Mock
	private SuporteDominioServico suporte;

	@InjectMocks
	private VendaServico vendaServico;

	@Test
	void deveImpedirTrocaDeClienteQuandoVeiculoAtualPertenceAOutroUsuario() {
		Long empresaId = 1L;
		Long vendaId = 10L;
		Long clienteAntigoId = 100L;
		Long clienteNovoId = 200L;

		Usuario clienteAntigo = new Usuario();
		clienteAntigo.setId(clienteAntigoId);
		clienteAntigo.getPerfis().add(PerfilUsuario.CLIENTE);

		Usuario clienteNovo = new Usuario();
		clienteNovo.setId(clienteNovoId);
		clienteNovo.getPerfis().add(PerfilUsuario.CLIENTE);

		Veiculo veiculo = new Veiculo();
		veiculo.setId(300L);
		veiculo.setProprietario(clienteAntigo);

		Venda venda = new Venda();
		venda.setId(vendaId);
		venda.setCliente(clienteAntigo);
		venda.setVeiculo(veiculo);

		VendaAtualizacaoDTO dto = new VendaAtualizacaoDTO();
		dto.setClienteId(clienteNovoId);

		when(suporte.buscarVendaDaEmpresa(empresaId, vendaId)).thenReturn(venda);
		when(suporte.buscarUsuarioDaEmpresa(empresaId, clienteNovoId)).thenReturn(clienteNovo);

		assertThrows(RequisicaoInvalidaException.class, () -> vendaServico.atualizar(empresaId, vendaId, dto));

		verify(suporte).validarPerfil(clienteNovo, PerfilUsuario.CLIENTE);
		verify(vendaRepositorio, never()).save(venda);
	}
}
