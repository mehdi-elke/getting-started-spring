package fr.baretto.inventory.dao.repository;

import fr.baretto.inventory.dao.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.uuid = :uuid")
    Optional<Product> getProductByUUID(@Param("uuid") String uuid);
}
