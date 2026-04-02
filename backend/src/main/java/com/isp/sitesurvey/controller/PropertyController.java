package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.request.PropertyRequest;
import com.isp.sitesurvey.dto.response.*;
import com.isp.sitesurvey.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PropertyResponse>> list(
            @RequestParam Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(propertyService.listByOrg(orgId,
            PageRequest.of(page, size, Sort.by("createdAt").descending())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody PropertyRequest req) {
        return ResponseEntity.ok(propertyService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}