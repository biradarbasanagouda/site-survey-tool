package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.EquipmentRequest;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final SpaceRepository spaceRepository;

    @Override
    @Transactional
    public Equipment create(EquipmentRequest req) {
        Space space = spaceRepository.findById(req.spaceId())
            .orElseThrow(() -> new ResourceNotFoundException("Space", req.spaceId()));
        Equipment eq = Equipment.builder()
            .space(space).type(req.type()).model(req.model()).vendor(req.vendor())
            .powerWatts(req.powerWatts()).heatLoadBtuh(req.heatLoadBtuh())
            .mounting(req.mounting()).geometryType(req.geometryType())
            .geometryWkt(req.geometryWkt()).serialNumber(req.serialNumber())
            .build();
        return equipmentRepository.save(eq);
    }

    @Override
    public Equipment getById(Long id) {
        return equipmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Equipment", id));
    }

    @Override
    public List<Equipment> listBySpace(Long spaceId) {
        return equipmentRepository.findBySpaceId(spaceId);
    }

    @Override
    @Transactional
    public Equipment update(Long id, EquipmentRequest req) {
        Equipment eq = getById(id);
        eq.setType(req.type()); eq.setModel(req.model()); eq.setVendor(req.vendor());
        eq.setPowerWatts(req.powerWatts()); eq.setHeatLoadBtuh(req.heatLoadBtuh());
        eq.setMounting(req.mounting()); eq.setSerialNumber(req.serialNumber());
        eq.setGeometryType(req.geometryType()); eq.setGeometryWkt(req.geometryWkt());
        return equipmentRepository.save(eq);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        equipmentRepository.delete(getById(id));
    }
}
