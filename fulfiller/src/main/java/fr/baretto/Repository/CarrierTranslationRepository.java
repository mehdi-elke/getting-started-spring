package fr.baretto.Repository;

import fr.baretto.Entity.Carrier;
import fr.baretto.Entity.Translations.CarrierTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarrierTranslationRepository extends JpaRepository<CarrierTranslation, UUID> {
    List<CarrierTranslation> findByCarrier(Carrier carrier);
    List<CarrierTranslation> findByCarrierAndLocale(Carrier carrier, String locale);
} 