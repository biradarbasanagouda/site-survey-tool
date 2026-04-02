package com.isp.sitesurvey.dto.response;
import com.isp.sitesurvey.entity.FileEntity;
import java.time.LocalDateTime;
public record FileResponse(
    Long id, String filename, String contentType,
    Long sizeBytes, String ownerType, Long ownerId,
    LocalDateTime createdAt
) {
    public static FileResponse from(FileEntity f) {
        return new FileResponse(
            f.getId(), f.getFilename(), f.getContentType(),
            f.getSizeBytes(), f.getOwnerType(), f.getOwnerId(),
            f.getCreatedAt()
        );
    }
}