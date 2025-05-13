package fr.baretto.inventory.service;

import fr.baretto.inventory.dao.model.Product;
import fr.baretto.inventory.dao.repository.ProductRepository;
import fr.baretto.inventory.utils.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(ProductDto product) {
        Product productEntity = new Product();
        productEntity.setId(product.getId());
        productEntity.setName(product.getName());

        return productRepository.save(productEntity);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
