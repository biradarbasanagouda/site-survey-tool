package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findByOrganizationId(Long orgId, Pageable pageable);
    List<Property> findByOrganizationId(Long orgId);
}