package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.request.EquipmentRequest;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;
    private final SpaceRepository spaceRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody EquipmentRequest req) {
        Space space = spaceRepository.findById(req.spaceId())
            .orElseThrow(() -> new ResourceNotFoundException("Space", req.spaceId()));
        Equipment eq = Equipment.builder()
            .space(space).type(req.type()).model(req.model()).vendor(req.vendor())
            .powerWatts(req.powerWatts()).heatLoadBtuh(req.heatLoadBtuh())
            .mounting(req.mounting()).geometryType(req.geometryType())
            .geometryWkt(req.geometryWkt()).serialNumber(req.serialNumber())
            .build();
        Equipment saved = equipmentRepository.save(eq);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "id", saved.getId(), "type", saved.getType(), "model", saved.getModel() != null ? saved.getModel() : "",
            "spaceId", saved.getSpace().getId()
        ));
    }

    @GetMapping
    public ResponseEntity<List<Equipment>> listBySpace(@RequestParam Long spaceId) {
        return ResponseEntity.ok(equipmentRepository.findBySpaceId(spaceId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Equipment eq = equipmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
        equipmentRepository.delete(eq);
        return ResponseEntity.noContent().build();
    }
}