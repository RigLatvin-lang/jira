package org.aur.jira.dto;

import jakarta.validation.constraints.NotBlank;
import org.aur.jira.domain.enums.TaskPriority;

import java.util.UUID;

public record TaskRequest(
        @NotBlank String title,
        String description,
        UUID assigneeId,
        TaskPriority priority,
        Integer estimateMinutes
) {}
