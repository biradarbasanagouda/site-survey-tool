package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.FloorRequest;
import com.isp.sitesurvey.dto.response.FloorResponse;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.FloorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;
    private final BuildingRepository buildingRepository;

    @Override
    @Transactional
    public FloorResponse create(FloorRequest req) {
        Building building = buildingRepository.findById(req.buildingId())
            .orElseThrow(() -> new ResourceNotFoundException("Building", req.buildingId()));
        Floor floor = Floor.builder()
            .building(building)
            .levelLabel(req.levelLabel())
            .elevationM(req.elevationM())
            .scaleRatio(req.scaleRatio())
            .anchorPoints(req.anchorPoints())
            .build();
        return FloorResponse.from(floorRepository.save(floor));
    }

    @Override
    public FloorResponse getById(Long id) {
        return FloorResponse.from(findOrThrow(id));
    }

    @Override
    public List<FloorResponse> listByBuilding(Long buildingId) {
        return floorRepository.findByBuildingIdOrderByElevationMAsc(buildingId)
            .stream().map(FloorResponse::from).toList();
    }

    @Override
    @Transactional
    public FloorResponse update(Long id, FloorRequest req) {
        Floor floor = findOrThrow(id);
        floor.setLevelLabel(req.levelLabel());
        if (req.elevationM() != null) floor.setElevationM(req.elevationM());
        floor.setScaleRatio(req.scaleRatio());
        floor.setAnchorPoints(req.anchorPoints());
        return FloorResponse.from(floorRepository.save(floor));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        floorRepository.delete(findOrThrow(id));
    }

    private Floor findOrThrow(Long id) {
        return floorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Floor", id));
    }
}
