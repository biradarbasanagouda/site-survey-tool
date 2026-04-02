package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.request.RfScanRequest;
import com.isp.sitesurvey.dto.response.FileResponse;
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
@RequestMapping("/api/rf-scans")
@RequiredArgsConstructor
public class RfScanController {

    private final RfScanRepository rfScanRepository;
    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;
    private final FileEntityRepository fileEntityRepository;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<RfScan> create(@Valid @RequestBody RfScanRequest req) {
        Property property = propertyRepository.findById(req.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property", req.propertyId()));
        Floor floor = req.floorId() != null
            ? floorRepository.findById(req.floorId()).orElse(null) : null;
        RfScan scan = RfScan.builder()
            .property(property).floor(floor)
            .tool(req.tool() != null ? req.tool() : com.isp.sitesurvey.enums.RfTool.MANUAL)
            .parsedJson(req.parsedJson())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(rfScanRepository.save(scan));
    }

    @PostMapping("/{id}/raw-file")
    public ResponseEntity<FileResponse> uploadRawFile(@PathVariable Long id,
                                                       @RequestParam("file") MultipartFile file) {
        RfScan scan = rfScanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("RfScan", id));
        FileResponse fr = fileStorageService.upload(file, "RF_SCAN", id);
        scan.setRawFile(fileEntityRepository.findById(fr.id()).orElse(null));
        rfScanRepository.save(scan);
        return ResponseEntity.ok(fr);
    }

    @GetMapping
    public ResponseEntity<List<RfScan>> list(@RequestParam Long propertyId,
                                               @RequestParam(required=false) Long floorId) {
        List<RfScan> scans = floorId != null
            ? rfScanRepository.findByPropertyIdAndFloorId(propertyId, floorId)
            : rfScanRepository.findByPropertyId(propertyId);
        return ResponseEntity.ok(scans);
    }
}