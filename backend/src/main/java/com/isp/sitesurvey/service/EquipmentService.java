package com.isp.sitesurvey.service;

import com.isp.sitesurvey.dto.request.EquipmentRequest;
import com.isp.sitesurvey.entity.Equipment;
import java.util.List;

public interface EquipmentService {
    Equipment create(EquipmentRequest req);
    Equipment getById(Long id);
    List<Equipment> listBySpace(Long spaceId);
    Equipment update(Long id, EquipmentRequest req);
    void delete(Long id);
}
