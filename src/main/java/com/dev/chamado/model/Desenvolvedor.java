package com.dev.chamado.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Desenvolvedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "email")
	private String email;

	@Column(name = "consultor")
	private boolean consultor;

	@Column(name = "custo")
	private BigDecimal custo;

	@OneToMany(mappedBy = "desenvolvedor")
	@JsonIgnore // Adicionado esta notação pois estava listando infinifamente um desenvolvedor
				// ao adicionado Chamado
	private List<Chamado> chamados;

	public Desenvolvedor(Long id, String nome, String email, boolean consultor, BigDecimal custo,
			List<Chamado> chamados) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.consultor = consultor;
		this.custo = custo;
		this.chamados = chamados;
	}

	@Override
	public String toString() {
		return "Desenvolvedor [id=" + id + ", nome=" + nome + ", email=" + email + ", consultor=" + consultor
				+ ", custo=" + custo + ", chamados=" + chamados + "]";
	}

	public Desenvolvedor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isConsultor() {
		return consultor;
	}

	public void setConsultor(boolean consultor) {
		this.consultor = consultor;
	}

	public BigDecimal getCusto() {
		return custo;
	}

	public void setCusto(BigDecimal custo) {
		this.custo = custo;
	}

	public List<Chamado> getChamados() {
		return chamados;
	}

	public void setChamados(List<Chamado> chamados) {
		this.chamados = chamados;
	}

}
