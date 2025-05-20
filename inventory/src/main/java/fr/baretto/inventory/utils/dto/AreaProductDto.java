package fr.baretto.inventory.utils.dto;

import fr.baretto.inventory.dao.model.Area;
import fr.baretto.inventory.dao.model.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AreaProductDto {
    private Long id;
    @NotNull
    @NotEmpty
    private Area area;
    @NotNull
    @NotEmpty
    private Product product;
    @NotNull
    @NotEmpty
    private long quantity;
}
