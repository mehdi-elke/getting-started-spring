package fr.baretto.inventory.service;

import fr.baretto.inventory.dao.model.Product;
import fr.baretto.inventory.dao.repository.ProductRepository;
import fr.baretto.inventory.utils.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductByUUID(String uuid) {
        return Optional.ofNullable(productRepository.getProductByUUID(uuid).orElseThrow(() -> new IllegalArgumentException("Product not found")));
    }

    public List<Product> getAllProductsFromCatalog() {
        RestTemplate restTemplate = new RestTemplate();
        List productCatalog = restTemplate.getForObject("http://localhost:8000/product", List.class);
        productCatalog.forEach(product -> {
            ProductDto productDto = (ProductDto) product;
            this.addProduct(productDto);
        });
        return productRepository.findAll();
    }

    public void addProduct(ProductDto product) {

        Product productEntity = new Product();
        productEntity.setId(product.getId());
        productEntity.setUuid(product.getUuid());
        productEntity.setName(product.getName());

        productRepository.save(productEntity);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
