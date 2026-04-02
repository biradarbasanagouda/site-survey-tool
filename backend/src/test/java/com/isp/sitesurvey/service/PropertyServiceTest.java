package com.isp.sitesurvey.service;

import com.isp.sitesurvey.dto.request.PropertyRequest;
import com.isp.sitesurvey.dto.response.PropertyResponse;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.impl.PropertyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock PropertyRepository propertyRepository;
    @Mock OrganizationRepository organizationRepository;
    @InjectMocks PropertyServiceImpl propertyService;

    @Test
    void getById_notFound_throws() {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> propertyService.getById(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_success() {
        Organization org = Organization.builder().id(1L).name("TestOrg").build();
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));

        Property saved = Property.builder().id(10L).organization(org).name("HQ").build();
        when(propertyRepository.save(any())).thenReturn(saved);

        PropertyRequest req = new PropertyRequest(1L, "HQ", null, null, null, null, null, null, null, null, null, null);
        PropertyResponse resp = propertyService.create(req);

        assertThat(resp.id()).isEqualTo(10L);
        assertThat(resp.name()).isEqualTo("HQ");
    }

    @Test
    void create_orgNotFound_throws() {
        when(organizationRepository.findById(999L)).thenReturn(Optional.empty());
        PropertyRequest req = new PropertyRequest(999L, "Site", null, null, null, null, null, null, null, null, null, null);
        assertThatThrownBy(() -> propertyService.create(req))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_success() {
        Property p = Property.builder().id(1L).build();
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(p));
        doNothing().when(propertyRepository).delete(p);
        propertyService.delete(1L);
        verify(propertyRepository).delete(p);
    }
}
