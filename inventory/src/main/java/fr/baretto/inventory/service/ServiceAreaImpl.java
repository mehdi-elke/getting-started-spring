package fr.baretto.inventory.service;

import fr.baretto.inventory.dao.model.Area;
import fr.baretto.inventory.dao.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAreaImpl implements ServiceArea {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public List<Area> getAllAreas() {
        return areaRepository.findAll();
    }

    @Override
    public Area getAreaById(Long id) {
        return areaRepository.findById(id).orElse(null);
    }

    @Override
    public Area addArea(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public Area updateArea(Long id, Area area) {
        area.setId(id);
        return areaRepository.save(area);
    }

    @Override
    public void deleteArea(Long id) {
        areaRepository.deleteById(id);
    }

    @Override
    public List<Area> searchAreas(String name) {
        return areaRepository.findByNameContainingIgnoreCase(name);
    }
}
