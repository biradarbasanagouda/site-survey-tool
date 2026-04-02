package com.isp.sitesurvey.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface ImportExportService {
    List<Map<String, String>> parseCsv(MultipartFile file);
    List<Map<String, String>> parseXlsx(MultipartFile file);
    int bulkImportSpaces(Long floorId, MultipartFile file);
}
