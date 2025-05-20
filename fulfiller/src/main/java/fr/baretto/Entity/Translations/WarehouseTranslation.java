package fr.baretto.Entity.Translations;

import fr.baretto.Entity.Warehouse;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "WarehouseTranslation")
public class WarehouseTranslation extends TranslatableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @JsonBackReference
    private Warehouse warehouse;

    public WarehouseTranslation() {}

    public WarehouseTranslation(Warehouse warehouse, String code, String label, String locale) {
        super(code, label, locale);
        this.warehouse = warehouse;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
