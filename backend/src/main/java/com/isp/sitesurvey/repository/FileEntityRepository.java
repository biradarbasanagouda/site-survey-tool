package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByOwnerTypeAndOwnerId(String ownerType, Long ownerId);
}