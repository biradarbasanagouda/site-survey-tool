package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.request.SpaceRequest;
import com.isp.sitesurvey.dto.response.SpaceResponse;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceRepository spaceRepository;
    private final FloorRepository floorRepository;

    @PostMapping
    public ResponseEntity<SpaceResponse> create(@Valid @RequestBody SpaceRequest req) {
        Floor floor = floorRepository.findById(req.floorId())
            .orElseThrow(() -> new ResourceNotFoundException("Floor", req.floorId()));
        Space space = Space.builder()
            .floor(floor).name(req.name()).type(req.type())
            .geometryType(req.geometryType()).geometryWkt(req.geometryWkt())
            .areaSqM(req.areaSqM()).elevationM(req.elevationM()).notes(req.notes())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(SpaceResponse.from(spaceRepository.save(space)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(SpaceResponse.from(spaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Space", id))));
    }

    @GetMapping
    public ResponseEntity<List<SpaceResponse>> listByFloor(@RequestParam Long floorId) {
        return ResponseEntity.ok(spaceRepository.findByFloorId(floorId)
            .stream().map(SpaceResponse::from).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceResponse> update(@PathVariable Long id, @Valid @RequestBody SpaceRequest req) {
        Space space = spaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Space", id));
        space.setName(req.name()); space.setType(req.type());
        space.setGeometryType(req.geometryType()); space.setGeometryWkt(req.geometryWkt());
        space.setAreaSqM(req.areaSqM()); space.setNotes(req.notes());
        return ResponseEntity.ok(SpaceResponse.from(spaceRepository.save(space)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        spaceRepository.delete(spaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Space", id)));
        return ResponseEntity.noContent().build();
    }
}