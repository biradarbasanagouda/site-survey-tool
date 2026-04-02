package com.isp.sitesurvey.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record ChecklistResponseRequest(
    @NotNull Long templateId,
    @NotBlank String targetType,
    @NotNull Long targetId,
    String answersJson,
    String photosManifest,
    boolean submit
) {}