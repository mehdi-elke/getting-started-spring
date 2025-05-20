package fr.baretto.inventory.service;

import fr.baretto.inventory.utils.dto.AreaDto;

import java.util.List;

public interface ServiceArea {
    List<AreaDto> getAllAreas();
    AreaDto getAreaById(Long id);
    AreaDto addArea(AreaDto area);
    AreaDto updateArea(Long id, AreaDto area);
    void deleteArea(Long id);
    List<AreaDto> searchAreas(String name);
}
