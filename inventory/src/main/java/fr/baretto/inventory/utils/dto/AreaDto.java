package fr.baretto.inventory.utils.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AreaDto {
    private Long id ;
    @NotEmpty
    @NotNull
    private String name ;
}
