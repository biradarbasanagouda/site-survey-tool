package com.isp.sitesurvey.service;
import com.isp.sitesurvey.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;
public interface FileStorageService {
    FileResponse upload(MultipartFile file, String ownerType, Long ownerId);
    byte[] download(Long fileId);
    void delete(Long fileId);
    String generatePresignedUrl(Long fileId, int expiryMinutes);
}