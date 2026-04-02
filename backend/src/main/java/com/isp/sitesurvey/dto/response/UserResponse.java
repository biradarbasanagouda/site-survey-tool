package com.isp.sitesurvey.dto.response;
import com.isp.sitesurvey.entity.User;
import java.time.LocalDateTime;
public record UserResponse(Long id, String email, String fullName, LocalDateTime createdAt) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.getCreatedAt());
    }
}