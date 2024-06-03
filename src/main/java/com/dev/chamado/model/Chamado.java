package com.dev.chamado.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Chamado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numero_ticket")
	private String numeroTicket;

	@Column(name = "responsavel")
	private String responsavel;

	@Column(name = "resumo")
	private String resumo;

	@Column(name = "status")
	private String status;

	@Column(name = "suporte")
	private String suporte;

	@Column(name = "descricao")
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "desenvolvedor_id", nullable = false)
	private Desenvolvedor desenvolvedor;

	@OneToMany(mappedBy = "chamado")
	@JsonManagedReference
	private List<Atendimento> atendimento;

	public Chamado() {
		super();
	}

	public Chamado(String numeroTicket, String responsavel, String resumo, String status, String suporte,
			String descricao, Desenvolvedor desenvolvedor) {
		this.numeroTicket = numeroTicket;
		this.responsavel = responsavel;
		this.resumo = resumo;
		this.status = status;
		this.suporte = suporte;
		this.descricao = descricao;
		this.desenvolvedor = desenvolvedor;
	}

	public void buscarNumeroTicket(String id) {
		try {
			String apiUrl = "https://api.movidesk.com/public/v1/tickets?token=056815C8-FC0D-45BC-BA7B-6C2DCA4D7AE1&id="
					+ id;
			URL url = new URL(apiUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			Scanner scanner = new Scanner(conn.getInputStream());
			StringBuilder response = new StringBuilder();
			while (scanner.hasNextLine()) {
				response.append(scanner.nextLine());
			}
			scanner.close();

			// Analisar a resposta da API e atualizar os atributos conforme necessário
			JSONObject jsonResponse = new JSONObject(response.toString());
			JSONArray tickets = jsonResponse.getJSONArray("tickets");
			if (tickets.length() > 0) {
				JSONObject ticket = tickets.getJSONObject(0); // Assumindo que há apenas um ticket com esse ID

				// Definir o número do ticket
				this.numeroTicket = ticket.getString("id");

				// Obter as ações do ticket
				JSONArray actions = ticket.getJSONArray("actions");

				// Verificar se existem ações e se a primeira ação está presente
				if (actions.length() > 0) {
					JSONObject firstAction = actions.getJSONObject(0);
					// Preencher a descrição com o conteúdo da primeira ação
					this.descricao = firstAction.getString("description");

					// Definir o resumo como os primeiros 50 caracteres da descrição
					if (this.descricao.length() > 50) {
						this.resumo = this.descricao.substring(0, 50);
					} else {
						this.resumo = this.descricao;
					}

					// Definir o status como "Novo"
					this.status = "Novo";
				} else {
					System.out.println("Nenhuma ação encontrada para o ticket.");
				}
			} else {
				System.out.println("Ticket não encontrado para o ID fornecido.");
			}

		} catch (IOException e) {
			// Lidar com erro de conexão ou leitura dos dados da API
			e.printStackTrace();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroTicket() {
		return numeroTicket;
	}

	public void setNumeroTicket(String numeroTicket) {
		this.numeroTicket = numeroTicket;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuporte() {
		return suporte;
	}

	public void setSuporte(String suporte) {
		this.suporte = suporte;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Desenvolvedor getDesenvolvedor() {
		return desenvolvedor;
	}

	public void setDesenvolvedor(Desenvolvedor desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
	}

	public List<Atendimento> getAtendimentos() {
		return atendimento;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimento = atendimentos;
	}

}
