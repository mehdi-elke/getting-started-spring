package fr.baretto.inventory.service;

import fr.baretto.inventory.dao.model.AreaProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaProductService {

    private final AreaProductRepository areaProductRepository;

    @Autowired
    public AreaProductService(AreaProductRepository areaProductRepository) {
        this.areaProductRepository = areaProductRepository;
    }

    public void addAreaProduct(AreaProduct areaProduct) {
        areaProductRepository.save(areaProduct);
    }

    public List<AreaProduct> getAllProducts() {
        return areaProductRepository.findAll();
    }
}
