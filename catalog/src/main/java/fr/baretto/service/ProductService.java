package fr.baretto.service;

import fr.baretto.data.Product;
import fr.baretto.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean deleteProduct(UUID productId){
        try {
            productRepository.deleteById(productId);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
