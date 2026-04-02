package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.service.ImportExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportExportController {

    private final ImportExportService importExportService;

    @PostMapping("/spaces/{floorId}")
    public ResponseEntity<Map<String, Object>> bulkImportSpaces(
            @PathVariable Long floorId,
            @RequestParam("file") MultipartFile file) {
        int count = importExportService.bulkImportSpaces(floorId, file);
        return ResponseEntity.ok(Map.of(
            "message", "Import complete",
            "imported", count,
            "floorId", floorId
        ));
    }

    @PostMapping("/preview/csv")
    public ResponseEntity<List<Map<String, String>>> previewCsv(
            @RequestParam("file") MultipartFile file) {
        List<Map<String, String>> rows = importExportService.parseCsv(file);
        return ResponseEntity.ok(rows.stream().limit(10).toList());
    }

    @PostMapping("/preview/xlsx")
    public ResponseEntity<List<Map<String, String>>> previewXlsx(
            @RequestParam("file") MultipartFile file) {
        List<Map<String, String>> rows = importExportService.parseXlsx(file);
        return ResponseEntity.ok(rows.stream().limit(10).toList());
    }
}
