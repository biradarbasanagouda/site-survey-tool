package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FloorRepository extends JpaRepository<Floor, Long> {
    List<Floor> findByBuildingId(Long buildingId);
    List<Floor> findByBuildingIdOrderByElevationMAsc(Long buildingId);
}