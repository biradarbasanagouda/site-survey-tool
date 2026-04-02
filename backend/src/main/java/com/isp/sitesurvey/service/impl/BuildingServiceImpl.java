package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.BuildingRequest;
import com.isp.sitesurvey.dto.response.BuildingResponse;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final PropertyRepository propertyRepository;

    @Override
    @Transactional
    public BuildingResponse create(BuildingRequest req) {
        Property property = propertyRepository.findById(req.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property", req.propertyId()));
        Building b = Building.builder()
            .property(property).name(req.name()).code(req.code())
            .footprintType(req.footprintType()).footprintWkt(req.footprintWkt())
            .floorsCount(req.floorsCount() != null ? req.floorsCount() : 1)
            .build();
        return BuildingResponse.from(buildingRepository.save(b));
    }

    @Override
    public BuildingResponse getById(Long id) {
        return BuildingResponse.from(findOrThrow(id));
    }

    @Override
    public List<BuildingResponse> listByProperty(Long propertyId) {
        return buildingRepository.findByPropertyId(propertyId)
            .stream().map(BuildingResponse::from).toList();
    }

    @Override
    @Transactional
    public BuildingResponse update(Long id, BuildingRequest req) {
        Building b = findOrThrow(id);
        b.setName(req.name()); b.setCode(req.code());
        b.setFootprintType(req.footprintType()); b.setFootprintWkt(req.footprintWkt());
        if (req.floorsCount() != null) b.setFloorsCount(req.floorsCount());
        return BuildingResponse.from(buildingRepository.save(b));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        buildingRepository.delete(findOrThrow(id));
    }

    private Building findOrThrow(Long id) {
        return buildingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Building", id));
    }
}