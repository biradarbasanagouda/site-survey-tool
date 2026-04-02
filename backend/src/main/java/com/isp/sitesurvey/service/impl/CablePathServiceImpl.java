package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.entity.CablePath;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.CablePathRepository;
import com.isp.sitesurvey.service.CablePathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CablePathServiceImpl implements CablePathService {

    private final CablePathRepository cablePathRepository;

    @Override
    @Transactional
    public CablePath create(CablePath cablePath) {
        return cablePathRepository.save(cablePath);
    }

    @Override
    public CablePath getById(Long id) {
        return cablePathRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CablePath", id));
    }

    @Override
    public List<CablePath> listByProperty(Long propertyId) {
        return cablePathRepository.findByPropertyId(propertyId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cablePathRepository.delete(getById(id));
    }
}
