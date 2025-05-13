package fr.baretto.Repository;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Enumeration.FulfillmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface FulfillmentOrderRepository extends JpaRepository<FulfillmentOrder, UUID> {
    List<FulfillmentOrder> findByStatus(FulfillmentStatus status);
    List<FulfillmentOrder> findByOrderReference(String orderReference);
    List<FulfillmentOrder> findByStatusAndOrderReference(FulfillmentStatus status, String orderReference);
    boolean existsByOrderReference(String orderReference);

    Optional<FulfillmentOrder> findById(UUID id);
}
