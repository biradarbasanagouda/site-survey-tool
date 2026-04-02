package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.RfScanRequest;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.enums.RfTool;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.RfScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RfScanServiceImpl implements RfScanService {

    private final RfScanRepository rfScanRepository;
    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;

    @Override
    @Transactional
    public RfScan create(RfScanRequest req) {
        Property property = propertyRepository.findById(req.propertyId())
            .orElseThrow(() -> new ResourceNotFoundException("Property", req.propertyId()));
        Floor floor = req.floorId() != null
            ? floorRepository.findById(req.floorId()).orElse(null) : null;
        RfScan scan = RfScan.builder()
            .property(property).floor(floor)
            .tool(req.tool() != null ? req.tool() : RfTool.MANUAL)
            .parsedJson(req.parsedJson())
            .build();
        return rfScanRepository.save(scan);
    }

    @Override
    public RfScan getById(Long id) {
        return rfScanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("RfScan", id));
    }

    @Override
    public List<RfScan> listByProperty(Long propertyId, Long floorId) {
        return floorId != null
            ? rfScanRepository.findByPropertyIdAndFloorId(propertyId, floorId)
            : rfScanRepository.findByPropertyId(propertyId);
    }
}
