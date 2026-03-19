package org.aur.jira.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aur.jira.domain.entity.User;
import org.aur.jira.domain.enums.TaskPriority;
import org.aur.jira.domain.enums.TaskStatus;
import org.aur.jira.dto.*;
import org.aur.jira.usecase.TaskUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskUseCase taskUseCase;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request,
                                                @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskUseCase.create(request, currentUser));
    }

    @GetMapping
    @Operation(summary = "Get all tasks with optional filters")
    public ResponseEntity<List<TaskResponse>> getAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) UUID assigneeId) {
        return ResponseEntity.ok(taskUseCase.getAll(status, priority, assigneeId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskUseCase.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    public ResponseEntity<TaskResponse> update(@PathVariable UUID id,
                                                @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskUseCase.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change task status (drag-and-drop)")
    public ResponseEntity<TaskResponse> changeStatus(@PathVariable UUID id,
                                                      @Valid @RequestBody StatusRequest request) {
        return ResponseEntity.ok(taskUseCase.changeStatus(id, request.status()));
    }

    @PatchMapping("/{id}/actual-time")
    @Operation(summary = "Log actual time spent on task")
    public ResponseEntity<TaskResponse> logActualTime(@PathVariable UUID id,
                                                       @Valid @RequestBody ActualTimeRequest request) {
        return ResponseEntity.ok(taskUseCase.logActualTime(id, request.minutes()));
    }
}
