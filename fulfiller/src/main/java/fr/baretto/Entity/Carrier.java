package fr.baretto.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.baretto.Entity.Translations.Translatable;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.baretto.Entity.Translations.CarrierTranslation;
import fr.baretto.Repository.CarrierTranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "carriers")
public class Carrier implements Translatable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "carrier_warehouse",
        joinColumns = @JoinColumn(name = "carrier_id"),
        inverseJoinColumns = @JoinColumn(name = "warehouse_id")
    )
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Set<Warehouse> includedWarehouses = new HashSet<>();

    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Set<CarrierTranslation> translations = new HashSet<>();

    @Transient
    @Autowired
    private CarrierTranslationRepository translationRepository;

    public Carrier() {
        this.includedWarehouses = new HashSet<>();
        this.translations = new HashSet<>();
    }

    public Carrier(String code, String name) {
        this();
        this.code = code;
        this.name = name;
    }

    @Override
    @JsonProperty("translations")
    public Map<String, String> getTranslations() {
        return translations.stream()
            .collect(Collectors.toMap(
                CarrierTranslation::getLocale,
                CarrierTranslation::getLabel
            ));
    }

    @Override
    public List<CarrierTranslation> getTranslationEntities() {
        return translations.stream().toList();
    }

    public void addTranslation(String locale, String label) {
        CarrierTranslation translation = new CarrierTranslation(this, code, label, locale);
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

    public Set<Warehouse> getIncludedWarehouses() {
        return includedWarehouses;
    }

    public void setIncludedWarehouses(Set<Warehouse> includedWarehouses) {
        this.includedWarehouses = includedWarehouses;
    }

}
