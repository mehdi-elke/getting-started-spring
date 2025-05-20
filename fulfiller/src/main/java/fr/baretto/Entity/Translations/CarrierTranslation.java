package fr.baretto.Entity.Translations;

import fr.baretto.Entity.Carrier;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "CarrierTranslation")
public class CarrierTranslation extends TranslatableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrier_id", nullable = false)
    @JsonBackReference
    private Carrier carrier;

    public CarrierTranslation() {}

    public CarrierTranslation(Carrier carrier, String code, String label, String locale) {
        super(code, label, locale);
        this.carrier = carrier;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }
}


