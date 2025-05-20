package fr.baretto.inventory.dao.repository;

import fr.baretto.inventory.dao.model.AreaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AreaProductRepository extends JpaRepository<AreaProduct, Long> {

    @Query("SELECT ap FROM AreaProduct ap WHERE ap.area.id = :areaId AND ap.product.id = :productId")
    List<AreaProduct> findAreaProductByProductId(@Param("productId") Long productId);

    @Query("SELECT ap FROM AreaProduct ap WHERE ap.product.id = :productId")
    Optional<AreaProduct> findByProductIdAndAreaId(@Param("productId") Long productId);
}
