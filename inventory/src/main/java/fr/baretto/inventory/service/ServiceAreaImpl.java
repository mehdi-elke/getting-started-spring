package fr.baretto.inventory.service;

import fr.baretto.inventory.dao.model.Area;
import fr.baretto.inventory.dao.repository.AreaRepository;
import fr.baretto.inventory.utils.dto.AreaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceAreaImpl implements ServiceArea {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public List<AreaDto> getAllAreas() {
        return areaRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AreaDto getAreaById(Long id) {
        return areaRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public AreaDto addArea(AreaDto areaDto) {
        if (areaRepository.existsByNameIgnoreCase(areaDto.getName())) {
            throw new IllegalArgumentException("La zone existe déjà");
        }

        Area area = toEntity(areaDto);
        Area saved = areaRepository.save(area);
        return toDto(saved);
    }

    @Override
    public AreaDto updateArea(Long id, AreaDto areaDto) {
        areaDto.setId(id);
        Area updated = areaRepository.save(toEntity(areaDto));
        return toDto(updated);
    }

    @Override
    public void deleteArea(Long id) {
        areaRepository.deleteById(id);
    }

    @Override
    public List<AreaDto> searchAreas(String name) {
        return areaRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AreaDto toDto(Area area) {
        if (area == null) return null;
        AreaDto dto = new AreaDto();
        dto.setId(area.getId());
        dto.setName(area.getName());
        return dto;
    }

    private Area toEntity(AreaDto dto) {
        if (dto == null) return null;
        Area area = new Area();
        area.setId(dto.getId());
        area.setName(dto.getName());
        return area;
    }
}
