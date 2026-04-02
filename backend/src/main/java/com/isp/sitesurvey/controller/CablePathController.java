package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.enums.CableMedium;
import com.isp.sitesurvey.enums.GeometryType;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.CablePathService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cable-paths")
@RequiredArgsConstructor
public class CablePathController {

    private final CablePathService cablePathService;
    private final PropertyRepository propertyRepository;
    private final SpaceRepository spaceRepository;

    @PostMapping
    public ResponseEntity<CablePath> create(@RequestBody Map<String, Object> body) {
        Long propertyId = Long.parseLong(body.get("propertyId").toString());
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new ResourceNotFoundException("Property", propertyId));

        CablePath cp = CablePath.builder()
            .property(property)
            .medium(CableMedium.valueOf(body.getOrDefault("medium", "FIBER_SM").toString()))
            .geometryType(GeometryType.LINESTRING)
            .geometryWkt(body.getOrDefault("geometryWkt", null) != null
                ? body.get("geometryWkt").toString() : null)
            .notes(body.getOrDefault("notes", null) != null
                ? body.get("notes").toString() : null)
            .lengthM(body.containsKey("lengthM")
                ? new BigDecimal(body.get("lengthM").toString()) : null)
            .build();

        if (body.containsKey("fromSpaceId")) {
            Long fsId = Long.parseLong(body.get("fromSpaceId").toString());
            spaceRepository.findById(fsId).ifPresent(cp::setFromSpace);
        }
        if (body.containsKey("toSpaceId")) {
            Long tsId = Long.parseLong(body.get("toSpaceId").toString());
            spaceRepository.findById(tsId).ifPresent(cp::setToSpace);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(cablePathService.create(cp));
    }

    @GetMapping
    public ResponseEntity<List<CablePath>> listByProperty(@RequestParam Long propertyId) {
        return ResponseEntity.ok(cablePathService.listByProperty(propertyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CablePath> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cablePathService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cablePathService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
