package fr.baretto.inventory.dao.repository;

import fr.baretto.inventory.dao.model.AreaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AreaProductRepository extends JpaRepository<AreaProduct, Integer> {

}
