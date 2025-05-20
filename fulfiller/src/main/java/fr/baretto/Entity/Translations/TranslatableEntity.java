package fr.baretto.Entity.Translations;

import jakarta.persistence.*;
import java.util.UUID;

@MappedSuperclass
public abstract class TranslatableEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private String locale;

    public TranslatableEntity() {}

    public TranslatableEntity(String code, String label, String locale) {
        this.code = code;
        this.label = label;
        this.locale = locale;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslatableEntity that = (TranslatableEntity) o;
        return code.equals(that.code) && locale.equals(that.locale);
    }

    @Override
    public int hashCode() {
        return 31 * code.hashCode() + locale.hashCode();
    }
}
