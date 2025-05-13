package fr.baretto.inventory.controller;

import fr.baretto.inventory.dao.model.AreaProduct;
import fr.baretto.inventory.service.AreaProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/area-product")
public class AreaProductController {

    private final AreaProductService areaProductService;

    @Autowired
    public AreaProductController(AreaProductService areaProductService) {
        this.areaProductService = areaProductService;
    }

    @PostMapping("/add")
    public void addAreaProduct(@RequestBody AreaProduct areaProduct) {
        areaProductService.addAreaProduct(areaProduct);
    }

    @GetMapping("/all")
    public List<AreaProduct> getAllProducts() {
        return areaProductService.getAllProducts();
    }
}
