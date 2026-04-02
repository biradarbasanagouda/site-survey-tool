package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.Membership;
import com.isp.sitesurvey.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByOrganizationIdAndUserId(Long orgId, Long userId);
    List<Membership> findByUserId(Long userId);
    List<Membership> findByOrganizationId(Long orgId);
}