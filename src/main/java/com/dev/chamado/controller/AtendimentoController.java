package com.dev.chamado.controller;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dev.chamado.model.Chamado;
import com.dev.chamado.repository.ChamadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.chamado.exception.ResourceNotFoundException;
import com.dev.chamado.model.Atendimento;
import com.dev.chamado.repository.AtendimentoRepository;

@RestController
@CrossOrigin(origins = "https://demanxier-chamados.up.railway.app/")
@RequestMapping("/api/v1")
public class AtendimentoController {

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Autowired
	private ChamadoRepository chamadoRepository;

	@GetMapping("/atendimento")
	public List<Atendimento> getAllAtendimento() {
		return atendimentoRepository.findAll();
	}

	@GetMapping("/atendimento/{id}")
	public ResponseEntity<Atendimento> getAtendimentoById(@PathVariable(value = "id") Long atendimentoId)
			throws ResourceNotFoundException {
		Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
				.orElseThrow(() -> new ResourceNotFoundException("Atendimento não encontrado: " + atendimentoId));
		return ResponseEntity.ok().body(atendimento);
	}

	@PostMapping("/atendimento")
	public ResponseEntity<?> createAtendimento(@Validated @RequestBody Atendimento atendimento) {
		Long chamadoId = atendimento.getChamado().getId();
		Optional<Chamado> optionalChamado = chamadoRepository.findByIdAndStatus(chamadoId, "Novo");

		if (optionalChamado.isEmpty()) {
			return ResponseEntity.badRequest().body("ID do chamado não existe ou já foi atendido");
		}

		Chamado chamado = optionalChamado.get();

		atendimento.setChamado(chamado);
		atendimento.setData(LocalDate.now());


		// Calcular o custo do atendimento
		if (chamado.getDesenvolvedor().isConsultor()) {
			BigDecimal custoPorHora = chamado.getDesenvolvedor().getCusto();
			Duration duration = Duration.between(atendimento.getHoraInicio(), atendimento.getHoraFim());
			long hours = duration.toHours();
			BigDecimal custoTotal = custoPorHora.multiply(BigDecimal.valueOf(hours));
			atendimento.setCusto(custoTotal);
		} else {
			atendimento.setCusto(BigDecimal.ZERO);
		}

		atendimentoRepository.save(atendimento);

		chamado.setStatus("Atendido");
		chamadoRepository.save(chamado);

		return ResponseEntity.ok("Atendimento realizado com sucesso");
	}

	@PutMapping("/atendimento/{id}")
	public ResponseEntity<Atendimento> updateAtendimento(@PathVariable(value = "id") Long atendimentoId,
			@Validated @RequestBody Atendimento atendimentoDatails)
			throws ResourceNotFoundException {
		Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
				.orElseThrow(
						() -> new ResourceNotFoundException("Atendimento não existe para esse ID: " + atendimentoId));

		atendimento.setData(atendimentoDatails.getData());
		atendimento.setHoraInicio(atendimentoDatails.getHoraInicio());
		atendimento.setHoraFim(atendimentoDatails.getHoraFim());
		atendimento.setDescricao(atendimentoDatails.getDescricao());
		atendimento.setCusto(atendimentoDatails.getCusto());
		final Atendimento updateAtendimento = atendimentoRepository.save(atendimento);
		return ResponseEntity.ok(updateAtendimento);
	}

	@DeleteMapping("/atendimento/{id}")
	public Map<String, Boolean> deleteAtendimento(@PathVariable(value = "id") Long atendimentoId)
			throws ResourceNotFoundException {
		Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
				.orElseThrow(() -> new ResourceNotFoundException("Atendimento não existe para o ID: " + atendimentoId));

		atendimentoRepository.delete(atendimento);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
