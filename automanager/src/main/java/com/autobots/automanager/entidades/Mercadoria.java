package com.autobots.automanager.entidades;

import java.util.Date;

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
public class Mercadoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Date validade;
	@Column(nullable = false)
	private Date fabricao;
	@Column(nullable = false)
	private Date cadastro;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private Long quantidade;
	@Column(nullable = false)
	private Double valor;
	@Column
	private String descricao;
	@ManyToOne
	@JoinColumn(name = "empresa_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Empresa empresa;
	@ManyToOne
	@JoinColumn(name = "fornecedor_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Usuario fornecedor;
}
