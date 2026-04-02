package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.response.FileResponse;
import com.isp.sitesurvey.entity.FileEntity;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.FileEntityRepository;
import com.isp.sitesurvey.service.FileStorageService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final FileEntityRepository fileEntityRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public FileResponse upload(MultipartFile file, String ownerType, Long ownerId) {
        try {
            ensureBucketExists();
            String storageKey = ownerType + "/" + ownerId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(storageKey)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

            FileEntity entity = FileEntity.builder()
                .ownerType(ownerType).ownerId(ownerId)
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .storageKey(storageKey)
                .sizeBytes(file.getSize())
                .build();

            return FileResponse.from(fileEntityRepository.save(entity));
        } catch (Exception e) {
            log.error("File upload failed", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] download(Long fileId) {
        FileEntity entity = fileEntityRepository.findById(fileId)
            .orElseThrow(() -> new ResourceNotFoundException("File", fileId));
        try {
            InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName).object(entity.getStorageKey()).build());
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("File download failed", e);
        }
    }

    @Override
    public void delete(Long fileId) {
        FileEntity entity = fileEntityRepository.findById(fileId)
            .orElseThrow(() -> new ResourceNotFoundException("File", fileId));
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName).object(entity.getStorageKey()).build());
            fileEntityRepository.delete(entity);
        } catch (Exception e) {
            throw new RuntimeException("File delete failed", e);
        }
    }

    @Override
    public String generatePresignedUrl(Long fileId, int expiryMinutes) {
        FileEntity entity = fileEntityRepository.findById(fileId)
            .orElseThrow(() -> new ResourceNotFoundException("File", fileId));
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName).object(entity.getStorageKey())
                .method(Method.GET).expiry(expiryMinutes, TimeUnit.MINUTES)
                .build());
        } catch (Exception e) {
            throw new RuntimeException("Presigned URL generation failed", e);
        }
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}