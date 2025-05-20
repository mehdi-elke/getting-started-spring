package fr.baretto.inventory.utils.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

@Data
public class ProductDto {
    @NonNull
    @NotEmpty
    private Long id;
    @NonNull
    @NotEmpty
    private String uuid;
    @NonNull
    @NotEmpty
    private String name;    
}
