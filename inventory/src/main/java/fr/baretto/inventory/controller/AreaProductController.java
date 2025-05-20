package fr.baretto.inventory.controller;

import fr.baretto.inventory.service.AreaProductService;
import fr.baretto.inventory.utils.dto.AreaProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/area-product")
public class AreaProductController {

    @Autowired
    private AreaProductService areaProductService;

    @PostMapping("/add")
    public ResponseEntity<AreaProductDto> addAreaProduct(@RequestBody AreaProductDto areaProductDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaProductService.addAreaProduct(areaProductDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AreaProductDto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(areaProductService.getAllProducts());
    }

    @GetMapping("/check/{productId}/{qty}")
    public ResponseEntity<String> getAreaProductById(@PathVariable("productId") String productId, @PathVariable("qty") Long qty) {
        return ResponseEntity.status(HttpStatus.OK).body(areaProductService.checkProduct(productId, qty));
    }
}
