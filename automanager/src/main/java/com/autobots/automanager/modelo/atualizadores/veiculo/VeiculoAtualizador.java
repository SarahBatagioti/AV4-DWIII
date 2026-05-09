package com.autobots.automanager.modelo.atualizadores.veiculo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelo.dto.veiculo.VeiculoDTO;

@Component
public class VeiculoAtualizador {

	public Veiculo criar(VeiculoDTO dto) {
		Veiculo veiculo = new Veiculo();
		atualizar(veiculo, dto);
		return veiculo;
	}

	public void atualizar(Veiculo veiculo, VeiculoDTO dto) {
		veiculo.setTipo(dto.getTipo());
		veiculo.setModelo(dto.getModelo());
		veiculo.setPlaca(dto.getPlaca());
	}
}
