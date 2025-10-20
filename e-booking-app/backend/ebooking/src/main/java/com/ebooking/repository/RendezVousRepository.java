package com.ebooking.repository;

import com.ebooking.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    @Query("SELECT r FROM RendezVous r WHERE r.prestataire.id = :prestataireId AND r.date = :date AND r.heure = :heure")
    Optional<RendezVous> findExistingRendezVous(
            @Param("prestataireId") Long prestataireId,
            @Param("date") LocalDate date,
            @Param("heure") LocalTime heure
    );

    @Query("SELECT r FROM RendezVous r WHERE r.client.id = :clientId AND r.date = :date AND r.heure = :heure AND r.statut != 'ANNULE'")
    Optional<RendezVous> findClientRendezVousAtSameTime(
            @Param("clientId") Long clientId,
            @Param("date") LocalDate date,
            @Param("heure") LocalTime heure
    );

    List<RendezVous> findByClientId(Long clientId);

    List<RendezVous> findByPrestataireId(Long prestataireId);
}
