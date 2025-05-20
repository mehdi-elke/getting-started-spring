package fr.baretto.inventory.dao.repository;

import fr.baretto.inventory.dao.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByNameContainingIgnoreCase(String name);
}
