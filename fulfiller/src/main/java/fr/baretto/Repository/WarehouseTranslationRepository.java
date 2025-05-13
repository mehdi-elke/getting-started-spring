package fr.baretto.Repository;

import fr.baretto.Entity.Warehouse;
import fr.baretto.Entity.Translations.WarehouseTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarehouseTranslationRepository extends JpaRepository<WarehouseTranslation, UUID> {
    List<WarehouseTranslation> findByWarehouse(Warehouse warehouse);
    List<WarehouseTranslation> findByWarehouseAndLocale(Warehouse warehouse, String locale);
} 