package fr.baretto.controller;

import fr.baretto.data.Product;
import fr.baretto.data.request.ProductRequestDto;
import fr.baretto.repository.ProductRepository;
import fr.baretto.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable UUID productId, @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(productId, productRequestDto);
    }

    @DeleteMapping("/{productId}")
    public boolean deleteProduct(@PathVariable UUID productId) {
        return productService.deleteProduct(productId);
    }
}
