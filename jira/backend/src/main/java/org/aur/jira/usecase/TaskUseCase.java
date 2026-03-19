package org.aur.jira.usecase;

import lombok.RequiredArgsConstructor;
import org.aur.jira.domain.entity.Task;
import org.aur.jira.domain.entity.User;
import org.aur.jira.domain.enums.TaskPriority;
import org.aur.jira.domain.enums.TaskStatus;
import org.aur.jira.dto.*;
import org.aur.jira.repository.TaskRepository;
import org.aur.jira.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskUseCase {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse create(TaskRequest request, User creator) {
        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .creator(creator)
                .assignee(assignee)
                .priority(request.priority() != null ? request.priority() : TaskPriority.MEDIUM)
                .estimateMinutes(request.estimateMinutes())
                .build();

        return TaskResponse.from(taskRepository.save(task));
    }

    public List<TaskResponse> getAll(TaskStatus status, TaskPriority priority, UUID assigneeId) {
        List<Task> tasks;

        if (status != null && assigneeId != null) {
            tasks = taskRepository.findByStatusAndAssigneeId(status, assigneeId);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);
        } else if (assigneeId != null) {
            tasks = taskRepository.findByAssigneeId(assigneeId);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream().map(TaskResponse::from).toList();
    }

    public TaskResponse getById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse update(UUID id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        task.setEstimateMinutes(request.estimateMinutes());

        if (request.assigneeId() != null) {
            User assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
            task.setAssignee(assignee);
        }

        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public void delete(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskResponse changeStatus(UUID id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setStatus(newStatus);
        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse logActualTime(UUID id, Integer minutes) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setActualMinutes(minutes);
        return TaskResponse.from(taskRepository.save(task));
    }
}
