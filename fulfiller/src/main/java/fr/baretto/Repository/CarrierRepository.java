package fr.baretto.Repository;

import fr.baretto.Entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, UUID> {
    Optional<Carrier> findById(UUID id);
    Optional<Carrier> findByCode(String code);
} 