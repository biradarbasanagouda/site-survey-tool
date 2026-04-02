package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.SpaceRequest;
import com.isp.sitesurvey.dto.response.SpaceResponse;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final FloorRepository floorRepository;

    @Override
    @Transactional
    public SpaceResponse create(SpaceRequest req) {
        Floor floor = floorRepository.findById(req.floorId())
            .orElseThrow(() -> new ResourceNotFoundException("Floor", req.floorId()));
        Space space = Space.builder()
            .floor(floor).name(req.name()).type(req.type())
            .geometryType(req.geometryType()).geometryWkt(req.geometryWkt())
            .areaSqM(req.areaSqM()).elevationM(req.elevationM()).notes(req.notes())
            .build();
        return SpaceResponse.from(spaceRepository.save(space));
    }

    @Override
    public SpaceResponse getById(Long id) {
        return SpaceResponse.from(findOrThrow(id));
    }

    @Override
    public List<SpaceResponse> listByFloor(Long floorId) {
        return spaceRepository.findByFloorId(floorId)
            .stream().map(SpaceResponse::from).toList();
    }

    @Override
    @Transactional
    public SpaceResponse update(Long id, SpaceRequest req) {
        Space space = findOrThrow(id);
        space.setName(req.name()); space.setType(req.type());
        space.setGeometryType(req.geometryType()); space.setGeometryWkt(req.geometryWkt());
        space.setAreaSqM(req.areaSqM()); space.setElevationM(req.elevationM());
        space.setNotes(req.notes());
        return SpaceResponse.from(spaceRepository.save(space));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        spaceRepository.delete(findOrThrow(id));
    }

    private Space findOrThrow(Long id) {
        return spaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Space", id));
    }
}
