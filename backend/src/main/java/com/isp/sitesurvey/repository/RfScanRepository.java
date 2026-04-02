package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.RfScan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RfScanRepository extends JpaRepository<RfScan, Long> {
    List<RfScan> findByPropertyId(Long propertyId);
    List<RfScan> findByPropertyIdAndFloorId(Long propertyId, Long floorId);
}