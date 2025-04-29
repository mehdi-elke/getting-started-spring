package fr.baretto.controller;

import fr.baretto.repository.ProductRepository;
import fr.baretto.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @DeleteMapping
    public boolean deleteProduct(UUID productId) {
        return productService.deleteProduct(productId);
    }
}
