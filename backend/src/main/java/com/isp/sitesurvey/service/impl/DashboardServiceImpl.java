package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.response.DashboardResponse;
import com.isp.sitesurvey.enums.ReportStatus;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PropertyRepository propertyRepository;
    private final BuildingRepository buildingRepository;
    private final FloorRepository floorRepository;
    private final SpaceRepository spaceRepository;
    private final EquipmentRepository equipmentRepository;
    private final ChecklistResponseRepository checklistResponseRepository;
    private final ReportRepository reportRepository;

    @Override
    public DashboardResponse getDashboard(Long orgId) {
        long properties = propertyRepository.findByOrganizationId(orgId).size();
        long buildings = buildingRepository.count();
        long floors = floorRepository.count();
        long spaces = spaceRepository.count();
        long equipment = equipmentRepository.count();
        long responses = checklistResponseRepository.count();
        long pendingReports = reportRepository.findByStatus(ReportStatus.PENDING).size()
            + reportRepository.findByStatus(ReportStatus.GENERATING).size();
        return new DashboardResponse(properties, buildings, floors, spaces, equipment, responses, pendingReports);
    }
}