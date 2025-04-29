package fr.baretto.service;

import fr.baretto.data.Product;
import fr.baretto.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }
    
    public boolean deleteProduct(UUID productId){
        try {
            productRepository.deleteById(productId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Product CreateProduct(ProductRequestDto product){

        productRepository.createProduct(product);

    }
}


