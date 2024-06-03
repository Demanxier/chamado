package com.dev.chamado.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Atendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data")
	private LocalDate data;

	@Column(name = "hora_inicio")
	private LocalTime horaInicio;

	@Column(name = "hora_fim")
	private LocalTime horaFim;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "custo")
	private BigDecimal custo;

	@ManyToOne
	@JoinColumn(name = "chamado_id", nullable = false)
	@JsonBackReference
	private Chamado chamado;

	public Atendimento(Long id, LocalDate data, LocalTime horaInicio, LocalTime horaFim,
			String descricao, BigDecimal custo, Chamado chamado) {
		super();
		this.id = id;
		this.data = data;
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.descricao = descricao;
		this.custo = custo;
		this.chamado = chamado;
	}

	public Atendimento() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Atendimento [id=" + id + ", data=" + data + ", horaInicio=" + horaInicio
				+ ", horaFim=" + horaFim + ", descricao=" + descricao + ", custo=" + custo + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getCusto() {
		return custo;
	}

	public void setCusto(BigDecimal custo) {
		this.custo = custo;
	}

	public Chamado getChamado() {
		return chamado;
	}

	public void setChamado(Chamado chamado) {
		this.chamado = chamado;
	}

}
