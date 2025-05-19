package fr.baretto.Repository;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Enumeration.FulfillmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface FulfillmentOrderRepository extends JpaRepository<FulfillmentOrder, UUID> {
    List<FulfillmentOrder> findByStatus(FulfillmentStatus status);
    Optional<FulfillmentOrder> findById(UUID id);
}
