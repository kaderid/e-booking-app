package com.ebooking.repository;

import com.ebooking.model.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {
    List<Disponibilite> findByPrestataireId(Long prestataireId);
}
