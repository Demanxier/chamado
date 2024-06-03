package com.dev.chamado.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.chamado.exception.ResourceNotFoundException;
import com.dev.chamado.model.Chamado;
import com.dev.chamado.repository.ChamadoRepository;

@RestController
@CrossOrigin(origins = "https://demanxier-chamados.up.railway.app/")
@RequestMapping("/api/v1")
public class ChamadoController {

	@Autowired
	private ChamadoRepository chamadoRepository;

	@GetMapping("/chamado/buscarNumeroTicket")
	public String buscarNumeroTicket(@RequestParam String id) {
		// Crie uma instância do seu modelo Chamado
		Chamado chamado = new Chamado();

		// Chame o método buscarNumeroTicket do modelo Chamado
		chamado.buscarNumeroTicket(id);

		// Retorne a descrição do chamado (ou outras informações que desejar)
		return chamado.getDescricao();
	}

	@GetMapping("/chamado")
	public List<Chamado> getAllChamado() {
		return chamadoRepository.findAll();
	}

	@GetMapping("/chamado/{id}")
	public ResponseEntity<Chamado> getChamadoById(@PathVariable(value = "id") Long chamadoId)
			throws ResourceNotFoundException {
		Chamado chamado = chamadoRepository.findById(chamadoId)
				.orElseThrow(() -> new ResourceNotFoundException("Chamado não existe" + chamadoId));
		return ResponseEntity.ok().body(chamado);
	}

	@PostMapping("/chamado")
	public Chamado createChamado(@Validated @RequestBody Chamado chamado) {

		return chamadoRepository.save(chamado);
	}

	@PutMapping("/chamado/{id}")
	public ResponseEntity<Chamado> updateChamado(@PathVariable(value = "id") Long chamadoId,
			@Validated @RequestBody Chamado chamadoDatails) throws ResourceNotFoundException {
		Chamado chamado = chamadoRepository.findById(chamadoId)
				.orElseThrow(() -> new ResourceNotFoundException("Chamado não existe neste ID" + chamadoId));

		chamado.setNumeroTicket(chamadoDatails.getNumeroTicket());
		chamado.setResponsavel(chamadoDatails.getResponsavel());
		chamado.setResumo(chamadoDatails.getResumo());
		chamado.setStatus(chamadoDatails.getStatus());
		chamado.setSuporte(chamadoDatails.getSuporte());
		chamado.setDescricao(chamadoDatails.getDescricao());
		chamado.setDesenvolvedor(chamadoDatails.getDesenvolvedor());
		final Chamado updaChamado = chamadoRepository.save(chamado);
		return ResponseEntity.ok(updaChamado);
	}

	@DeleteMapping("/chamado/{id}")
	public Map<String, Boolean> deleteChamado(@PathVariable(value = "id") Long chamadoId)
			throws ResourceNotFoundException {
		Chamado chamado = chamadoRepository.findById(chamadoId)
				.orElseThrow(() -> new ResourceNotFoundException("Chamado não existe para i ID: " + chamadoId));

		chamadoRepository.delete(chamado);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);

		return response;
	}
}
