package com.dev.chamado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.chamado.model.Chamado;

import java.util.Optional;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    Optional<Chamado> findByIdAndStatus(Long id, String status);
}
