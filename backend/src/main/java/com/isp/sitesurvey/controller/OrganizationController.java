package com.isp.sitesurvey.controller;

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
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;

    @PostMapping
    public ResponseEntity<Organization> create(@RequestBody Map<String, String> body) {
        Organization org = Organization.builder().name(body.get("name")).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationRepository.save(org));
    }

    @GetMapping
    public ResponseEntity<List<Organization>> list() {
        return ResponseEntity.ok(organizationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Organization", id)));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<Membership>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok(membershipRepository.findByOrganizationId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Organization org = organizationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Organization", id));
        org.setName(body.get("name"));
        return ResponseEntity.ok(organizationRepository.save(org));
    }
}
