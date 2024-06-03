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
import org.springframework.web.bind.annotation.RestController;

import com.dev.chamado.exception.ResourceNotFoundException;
import com.dev.chamado.model.Desenvolvedor;
import com.dev.chamado.repository.DesenvolvedorRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class DesenvolvedorController {

	@Autowired
	private DesenvolvedorRepository desenvolvedorRepository;

	@GetMapping("/dev")
	public List<Desenvolvedor> getAllDesenvolvedors() {
		return desenvolvedorRepository.findAll();
	}

	@GetMapping("/dev/{id}")
	public ResponseEntity<Desenvolvedor> getDesenvolvedorById(@PathVariable(value = "id") Long desenvolvedorId)
			throws ResourceNotFoundException {
		Desenvolvedor desenvolvedor = desenvolvedorRepository.findById(desenvolvedorId)
				.orElseThrow(() -> new ResourceNotFoundException("Desenvolvedor não existe: " + desenvolvedorId));
		return ResponseEntity.ok().body(desenvolvedor);
	}

	@PostMapping("/dev")
	public Desenvolvedor createDesenvolvedor(@Validated @RequestBody Desenvolvedor desenvolvedor) {
		return desenvolvedorRepository.save(desenvolvedor);
	}

	@PutMapping("/dev/{id}")
	public ResponseEntity<Desenvolvedor> updateDesenvolvedor(@PathVariable(value = "id") Long desenvolvedorId,
			@Validated @RequestBody Desenvolvedor desenvolvedorDetails) throws ResourceNotFoundException {
		Desenvolvedor desenvolvedor = desenvolvedorRepository.findById(desenvolvedorId)
				.orElseThrow(() -> new ResourceNotFoundException("Dev não existe neste ID:" + desenvolvedorId));

		desenvolvedor.setNome(desenvolvedorDetails.getNome());
		desenvolvedor.setEmail(desenvolvedorDetails.getEmail());
		desenvolvedor.setConsultor(desenvolvedorDetails.isConsultor());
		desenvolvedor.setCusto(desenvolvedorDetails.getCusto());
		desenvolvedor.setChamados(desenvolvedorDetails.getChamados());
		final Desenvolvedor updateDesenvolvedor = desenvolvedorRepository.save(desenvolvedor);
		return ResponseEntity.ok(updateDesenvolvedor);
	}

	@DeleteMapping("/dev/{id}")
	public Map<String, Boolean> deleteDesenvolvedor(@PathVariable(value = "id") Long desenvolvedorId)
			throws ResourceNotFoundException {
		Desenvolvedor desenvolvedor = desenvolvedorRepository.findById(desenvolvedorId)
				.orElseThrow(() -> new ResourceNotFoundException("Dev não existe neste ID:" + desenvolvedorId));

		desenvolvedorRepository.delete(desenvolvedor);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
