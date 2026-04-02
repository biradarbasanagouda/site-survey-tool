package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findBySpaceId(Long spaceId);
}