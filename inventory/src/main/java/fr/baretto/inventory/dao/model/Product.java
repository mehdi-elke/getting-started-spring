package fr.baretto.inventory.dao.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "uuid", nullable = false)
    private String uuid;
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "product")
    private Set<AreaProduct> areaProducts = new HashSet<>();
}
