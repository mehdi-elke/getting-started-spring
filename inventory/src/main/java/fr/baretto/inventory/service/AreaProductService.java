package fr.baretto.inventory.service;

import fr.baretto.inventory.dao.model.Area;
import fr.baretto.inventory.dao.model.AreaProduct;
import fr.baretto.inventory.dao.model.Product;
import fr.baretto.inventory.dao.repository.AreaProductRepository;
import fr.baretto.inventory.dao.repository.AreaRepository;
import fr.baretto.inventory.dao.repository.ProductRepository;
import fr.baretto.inventory.utils.dto.AreaProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaProductService {

    @Autowired
    private AreaProductRepository areaProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private ProductService productService;

    public AreaProductDto addAreaProduct(AreaProductDto areaProductDto) {
        if (areaProductDto.getArea() == null || areaProductDto.getProduct() == null) {
            throw new IllegalArgumentException("Area and Product cannot be null");
        }

        if (areaProductDto.getArea().getId() == null || areaProductDto.getProduct().getId() == null) {
            throw new IllegalArgumentException("Area ID and Product ID cannot be null");
        }

        // Check if the product exist in the database
        Product product = productRepository.findById(areaProductDto.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Check if the product exist in the database
        Area area = areaRepository.findById(areaProductDto.getArea().getId())
                .orElseThrow(() -> new IllegalArgumentException("Area not found"));

        if (areaProductDto.getQuantity() == 0) {
            throw new IllegalArgumentException("Quantity cannot be zero");
        }

        if (areaProductDto.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        AreaProduct areaProduct = new AreaProduct();
        areaProduct.setArea(areaProductDto.getArea());
        areaProduct.setProduct(areaProductDto.getProduct());
        areaProduct.setQuantity(areaProductDto.getQuantity());
        areaProduct = areaProductRepository.save(areaProduct);
        areaProductDto.setId(areaProduct.getId());
        return areaProductDto;
    }

    public List<AreaProductDto> getAllProducts() {
        List<AreaProduct> areaProducts = areaProductRepository.findAll();
        List<AreaProductDto> areaProductDto = new ArrayList<>();
        areaProducts.forEach(areaProduct -> {
            AreaProductDto areaProductDto1 = new AreaProductDto();
            areaProductDto1.setId(areaProduct.getId());
            areaProductDto1.setArea(areaProduct.getArea());
            areaProductDto1.setProduct(areaProduct.getProduct());
            areaProductDto1.setQuantity(areaProduct.getQuantity());
            areaProductDto.add(areaProductDto1);
        });

        return areaProductDto;
    }

    public String checkProduct(String productUUID, Long qty) {
        Product product = productService.getProductByUUID(productUUID)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        List<AreaProduct> areaProducts = areaProductRepository.findAreaProductByProductId(product.getId());

        if (areaProducts == null) {
            return "Product not found in stock";
        }
        boolean checkQty = false;
        AreaProduct areaProductFound = new AreaProduct();
        for (AreaProduct areaProduct : areaProducts) {
            if (areaProduct.getQuantity() >= qty) {
                checkQty = true;
                areaProductFound = areaProduct;
                break;
            }
        }

        if (!checkQty) {
            return "The quantity requested is higher than required !";
        }

        areaProductFound.setQuantity(areaProductFound.getQuantity() - qty);
        areaProductRepository.save(areaProductFound);

        return "The product is available in stock !";
    }
}
