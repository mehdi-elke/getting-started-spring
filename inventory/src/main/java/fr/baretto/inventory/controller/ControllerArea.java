package fr.baretto.inventory.controller;
import fr.baretto.inventory.utils.dto.AreaDto;


import fr.baretto.inventory.dao.model.Area;
import fr.baretto.inventory.service.ServiceArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import io.swagger.annotations.Api;
import java.util.List;

@RestController
@RequestMapping("/api/area")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@Api(tags = "Area")
//@ApiResponses(value = {
//        @ApiResponse(code = 200, message = "OK"),
//        @ApiResponse(code = 400, message = "Bad Request"),
//        @ApiResponse(code = 401, message = "Unauthorized"),
//        @ApiResponse(code = 403, message = "Forbidden"),
//        @ApiResponse(code = 404, message = "Not Found"),
//        @ApiResponse(code = 500, message = "Internal Server Error")
//})

public class ControllerArea {
    @Autowired
    private ServiceArea serviceArea;

    @GetMapping("/all")
    public ResponseEntity<List<AreaDto>> getAllAreas() {
        List<AreaDto> areas = serviceArea.getAllAreas();
        return new ResponseEntity<>(areas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaDto> getAreaById(@PathVariable("id") Long id) {
        AreaDto area = serviceArea.getAreaById(id);
        return new ResponseEntity<>(area, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addArea(@RequestBody AreaDto area) {
        try {
            AreaDto createdArea = serviceArea.addArea(area);
            return new ResponseEntity<>(createdArea, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AreaDto> updateArea(@PathVariable("id") Long id, @RequestBody AreaDto area) {
        AreaDto updatedArea = serviceArea.updateArea(id, area);
        return new ResponseEntity<>(updatedArea, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable("id") Long id) {
        serviceArea.deleteArea(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/search")
    public ResponseEntity<List<AreaDto>> searchAreas(@RequestParam String name) {
        List<AreaDto> areas = serviceArea.searchAreas(name);
        return new ResponseEntity<>(areas, HttpStatus.OK);

    } 
}
