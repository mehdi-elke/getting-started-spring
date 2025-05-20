package fr.baretto.inventory.service;

import fr.baretto.inventory.dao.model.Area;

import java.util.List;

public interface ServiceArea {
    List<Area> getAllAreas();
    Area getAreaById(Long id);
    Area addArea(Area area);
    Area updateArea(Long id, Area area);
    void deleteArea(Long id);
    List<Area> searchAreas(String name);
}
