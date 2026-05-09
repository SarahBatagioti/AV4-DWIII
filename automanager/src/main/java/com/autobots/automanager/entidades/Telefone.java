package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
public class Telefone {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String ddd;
	@Column(nullable = false)
	private String numero;
	@ManyToOne
	@JoinColumn(name = "empresa_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Empresa empresa;
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Usuario usuario;
}
