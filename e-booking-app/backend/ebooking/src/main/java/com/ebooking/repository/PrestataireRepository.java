package com.ebooking.repository;

import com.ebooking.model.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestataireRepository extends JpaRepository<Prestataire, Long> {
}