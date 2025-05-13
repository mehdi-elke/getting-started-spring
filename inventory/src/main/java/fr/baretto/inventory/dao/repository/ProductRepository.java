package fr.baretto.inventory.dao.repository;

import fr.baretto.inventory.dao.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
