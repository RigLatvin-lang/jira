package org.aur.jira.dto;

import org.aur.jira.domain.entity.Task;
import org.aur.jira.domain.enums.TaskPriority;
import org.aur.jira.domain.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        UUID creatorId,
        String creatorUsername,
        UUID assigneeId,
        String assigneeUsername,
        TaskStatus status,
        TaskPriority priority,
        Integer estimateMinutes,
        Integer actualMinutes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreator().getId(),
                task.getCreator().getUsername(),
                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getAssignee() != null ? task.getAssignee().getUsername() : null,
                task.getStatus(),
                task.getPriority(),
                task.getEstimateMinutes(),
                task.getActualMinutes(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
