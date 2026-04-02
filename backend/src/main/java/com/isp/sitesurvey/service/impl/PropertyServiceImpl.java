package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.PropertyRequest;
import com.isp.sitesurvey.dto.response.*;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public PropertyResponse create(PropertyRequest req) {
        Organization org = organizationRepository.findById(req.organizationId())
            .orElseThrow(() -> new ResourceNotFoundException("Organization", req.organizationId()));
        Property p = Property.builder()
            .organization(org).name(req.name())
            .addressLine1(req.addressLine1()).addressLine2(req.addressLine2())
            .city(req.city()).state(req.state())
            .postalCode(req.postalCode()).country(req.country())
            .boundaryType(req.boundaryType()).boundaryWkt(req.boundaryWkt())
            .centroidLat(req.centroidLat()).centroidLon(req.centroidLon())
            .build();
        return PropertyResponse.from(propertyRepository.save(p));
    }

    @Override
    public PropertyResponse getById(Long id) {
        return PropertyResponse.from(findOrThrow(id));
    }

    @Override
    public PageResponse<PropertyResponse> listByOrg(Long orgId, Pageable pageable) {
        return PageResponse.from(propertyRepository.findByOrganizationId(orgId, pageable)
            .map(PropertyResponse::from));
    }

    @Override
    @Transactional
    public PropertyResponse update(Long id, PropertyRequest req) {
        Property p = findOrThrow(id);
        p.setName(req.name());
        p.setAddressLine1(req.addressLine1()); p.setAddressLine2(req.addressLine2());
        p.setCity(req.city()); p.setState(req.state());
        p.setPostalCode(req.postalCode()); p.setCountry(req.country());
        p.setBoundaryType(req.boundaryType()); p.setBoundaryWkt(req.boundaryWkt());
        p.setCentroidLat(req.centroidLat()); p.setCentroidLon(req.centroidLon());
        return PropertyResponse.from(propertyRepository.save(p));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        propertyRepository.delete(findOrThrow(id));
    }

    private Property findOrThrow(Long id) {
        return propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property", id));
    }
}