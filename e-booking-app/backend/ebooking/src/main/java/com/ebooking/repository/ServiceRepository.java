package com.ebooking.repository;

import com.ebooking.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    Optional<ServiceEntity> findByNom(String nom);
    List<ServiceEntity> findByPrestataireId(Long prestataireId);
}
