package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.request.FloorRequest;
import com.isp.sitesurvey.dto.response.*;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
public class FloorController {

    private final FloorRepository floorRepository;
    private final BuildingRepository buildingRepository;
    private final FileEntityRepository fileEntityRepository;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<FloorResponse> create(@Valid @RequestBody FloorRequest req) {
        Building building = buildingRepository.findById(req.buildingId())
            .orElseThrow(() -> new ResourceNotFoundException("Building", req.buildingId()));
        Floor floor = Floor.builder()
            .building(building).levelLabel(req.levelLabel())
            .elevationM(req.elevationM()).scaleRatio(req.scaleRatio())
            .anchorPoints(req.anchorPoints()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(FloorResponse.from(floorRepository.save(floor)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(FloorResponse.from(floorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Floor", id))));
    }

    @GetMapping
    public ResponseEntity<List<FloorResponse>> listByBuilding(@RequestParam Long buildingId) {
        return ResponseEntity.ok(floorRepository.findByBuildingIdOrderByElevationMAsc(buildingId)
            .stream().map(FloorResponse::from).toList());
    }

    @PostMapping("/{id}/plan")
    public ResponseEntity<FileResponse> uploadFloorPlan(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) {
        Floor floor = floorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Floor", id));
        FileResponse fileResponse = fileStorageService.upload(file, "FLOOR", id);
        FileEntity fileEntity = fileEntityRepository.findById(fileResponse.id())
            .orElseThrow(() -> new ResourceNotFoundException("File", fileResponse.id()));
        floor.setPlanFile(fileEntity);
        floorRepository.save(floor);
        return ResponseEntity.ok(fileResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Floor floor = floorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Floor", id));
        floorRepository.delete(floor);
        return ResponseEntity.noContent().build();
    }
}