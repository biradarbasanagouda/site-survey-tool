package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.request.BuildingRequest;
import com.isp.sitesurvey.dto.response.BuildingResponse;
import com.isp.sitesurvey.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping
    public ResponseEntity<BuildingResponse> create(@Valid @RequestBody BuildingRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(buildingService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(buildingService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BuildingResponse>> listByProperty(@RequestParam Long propertyId) {
        return ResponseEntity.ok(buildingService.listByProperty(propertyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody BuildingRequest req) {
        return ResponseEntity.ok(buildingService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        buildingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}