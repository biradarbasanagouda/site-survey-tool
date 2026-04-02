package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.response.FileResponse;
import com.isp.sitesurvey.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> upload(@RequestParam("file") MultipartFile file,
                                                @RequestParam String ownerType,
                                                @RequestParam Long ownerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(fileStorageService.upload(file, ownerType, ownerId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        byte[] data = fileStorageService.download(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
            .body(data);
    }

    @GetMapping("/{id}/url")
    public ResponseEntity<String> presignedUrl(@PathVariable Long id,
                                                @RequestParam(defaultValue = "60") int expiryMinutes) {
        return ResponseEntity.ok(fileStorageService.generatePresignedUrl(id, expiryMinutes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fileStorageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}