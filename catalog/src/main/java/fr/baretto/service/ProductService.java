package fr.baretto.service;

import fr.baretto.data.Product;
import fr.baretto.data.request.ProductRequestDto;
import fr.baretto.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequestDto productRequest){
        Product product = new Product(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice());
        return productRepository.save(product);
    }

    public Product getProduct(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public boolean deleteProduct(UUID productId){
        try {
            productRepository.deleteById(productId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Product updateProduct(UUID productId, ProductRequestDto productRequestDto){
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setName(productRequestDto.getName());
            product.setDescription(productRequestDto.getDescription());
            product.setPrice(productRequestDto.getPrice());
            return productRepository.save(product);
        } else {
            return null;
        }
    }
}


