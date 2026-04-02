package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findByPropertyId(Long propertyId);
}