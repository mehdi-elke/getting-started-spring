package fr.baretto.Entity;

import com.fasterxml.jackson.annotation.*;
import fr.baretto.Entity.Translations.Translatable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.baretto.Entity.Translations.WarehouseTranslation;

@Entity
@Table(name = "warehouse")
public class Warehouse implements Translatable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String marketplace;

    @ManyToMany(mappedBy = "includedWarehouses")
    @JsonBackReference
    private Set<Carrier> carriers = new HashSet<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Set<WarehouseTranslation> translations = new HashSet<>();

    @Override
    @JsonProperty("translations")
    public Map<String, String> getTranslations() {
        return translations.stream()
            .collect(Collectors.toMap(
                WarehouseTranslation::getLocale,
                WarehouseTranslation::getLabel
            ));
    }

    @Override
    public List<WarehouseTranslation> getTranslationEntities() {
        return translations.stream().toList();
    }

    public void addTranslation(String locale, String label) {
        WarehouseTranslation translation = new WarehouseTranslation(this, code, label, locale);
        translations.add(translation);
    }

    public void removeTranslation(String locale) {
        translations.removeIf(t -> t.getLocale().equals(locale));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public Set<Carrier> getCarriers() {
        return carriers;
    }

    public void setCarriers(Set<Carrier> carriers) {
        this.carriers = carriers;
    }
}
