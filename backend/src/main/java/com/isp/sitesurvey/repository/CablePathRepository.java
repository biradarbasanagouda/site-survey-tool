package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.CablePath;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CablePathRepository extends JpaRepository<CablePath, Long> {
    List<CablePath> findByPropertyId(Long propertyId);
}