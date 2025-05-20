package fr.baretto.inventory.controller;

import fr.baretto.inventory.dao.model.Product;
import fr.baretto.inventory.service.ProductService;
import fr.baretto.inventory.utils.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProductsFromCatalog() {
        return productService.getAllProductsFromCatalog();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
