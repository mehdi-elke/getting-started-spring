package fr.baretto.getting_started_spring.repository;

import fr.baretto.getting_started_spring.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    
}
