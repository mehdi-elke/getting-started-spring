package fr.baretto.getting_started_spring.controller;


import fr.baretto.getting_started_spring.data.Product;
import fr.baretto.getting_started_spring.data.request.ProductRequestDto;
import fr.baretto.getting_started_spring.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("{productId}")
    public Product getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @PostMapping
    public Product createProduct(@RequestBody @Valid ProductRequestDto productRequestDto){
        return productService.createProduct(productRequestDto);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(productId, productRequestDto);
    }

    @DeleteMapping("/{productId}")
    public boolean deleteProduct(@PathVariable UUID productId) {
        return productService.deleteProduct(productId);
    }


}
