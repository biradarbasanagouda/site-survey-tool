package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.enums.SpaceType;
import com.isp.sitesurvey.exception.BadRequestException;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.ImportExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportExportServiceImpl implements ImportExportService {

    private final SpaceRepository spaceRepository;
    private final FloorRepository floorRepository;

    @Override
    public List<Map<String, String>> parseCsv(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .parse(reader);
            List<Map<String, String>> rows = new ArrayList<>();
            for (CSVRecord record : parser) {
                rows.add(record.toMap());
            }
            return rows;
        } catch (IOException e) {
            throw new BadRequestException("Failed to parse CSV: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> parseXlsx(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) throw new BadRequestException("Empty spreadsheet");

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim().toLowerCase());
            }

            List<Map<String, String>> rows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> record = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    record.put(headers.get(j), cell == null ? "" : cellToString(cell));
                }
                rows.add(record);
            }
            return rows;
        } catch (IOException e) {
            throw new BadRequestException("Failed to parse XLSX: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public int bulkImportSpaces(Long floorId, MultipartFile file) {
        Floor floor = floorRepository.findById(floorId)
            .orElseThrow(() -> new ResourceNotFoundException("Floor", floorId));

        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        List<Map<String, String>> rows = filename.endsWith(".xlsx") || filename.endsWith(".xls")
            ? parseXlsx(file)
            : parseCsv(file);

        int count = 0;
        for (Map<String, String> row : rows) {
            String name = row.getOrDefault("name", "").trim();
            if (name.isEmpty()) continue;

            SpaceType type = SpaceType.OTHER;
            try {
                String rawType = row.getOrDefault("type", "OTHER").toUpperCase().trim();
                type = SpaceType.valueOf(rawType);
            } catch (IllegalArgumentException ignored) {}

            BigDecimal area = null;
            try { area = new BigDecimal(row.getOrDefault("area_sq_m", "")); } catch (Exception ignored) {}

            Space space = Space.builder()
                .floor(floor).name(name).type(type)
                .areaSqM(area).notes(row.getOrDefault("notes", null))
                .build();
            spaceRepository.save(space);
            count++;
        }
        log.info("Bulk imported {} spaces into floor {}", count, floorId);
        return count;
    }

    private String cellToString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                ? cell.getDateCellValue().toString()
                : String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default      -> "";
        };
    }
}
