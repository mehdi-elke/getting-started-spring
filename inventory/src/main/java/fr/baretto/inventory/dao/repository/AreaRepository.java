package fr.baretto.inventory.dao.repository;

import fr.baretto.inventory.dao.model.Area;
import fr.baretto.inventory.utils.dto.AreaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    boolean existsByNameIgnoreCase(String name);

    List<Area> findByNameContainingIgnoreCase(String name);
}
