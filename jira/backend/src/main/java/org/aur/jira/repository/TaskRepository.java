package org.aur.jira.repository;

import org.aur.jira.domain.entity.Task;
import org.aur.jira.domain.enums.TaskPriority;
import org.aur.jira.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByAssigneeId(UUID assigneeId);
    List<Task> findByCreatorId(UUID creatorId);
    List<Task> findByStatusAndAssigneeId(TaskStatus status, UUID assigneeId);
}
