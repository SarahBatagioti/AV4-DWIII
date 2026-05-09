package com.autobots.automanager.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Venda;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {
	Optional<Venda> findByIdentificacao(String identificacao);
}
