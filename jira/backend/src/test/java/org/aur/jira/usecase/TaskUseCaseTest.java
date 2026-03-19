package org.aur.jira.usecase;

import org.aur.jira.domain.entity.Task;
import org.aur.jira.domain.entity.User;
import org.aur.jira.domain.enums.TaskPriority;
import org.aur.jira.domain.enums.TaskStatus;
import org.aur.jira.dto.TaskRequest;
import org.aur.jira.dto.TaskResponse;
import org.aur.jira.repository.TaskRepository;
import org.aur.jira.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskUseCase Unit Tests")
class TaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskUseCase taskUseCase;

    private User creator;
    private User assignee;
    private Task task;
    private UUID taskId;
    private UUID assigneeId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        assigneeId = UUID.randomUUID();

        creator = User.builder()
                .id(UUID.randomUUID())
                .username("creator")
                .email("creator@test.com")
                .password("hashedpassword")
                .createdAt(LocalDateTime.now())
                .build();

        assignee = User.builder()
                .id(assigneeId)
                .username("assignee")
                .email("assignee@test.com")
                .password("hashedpassword")
                .createdAt(LocalDateTime.now())
                .build();

        task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .creator(creator)
                .assignee(assignee)
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.HIGH)
                .estimateMinutes(120)
                .actualMinutes(60)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create task with all fields")
    void shouldCreateTaskWithAllFields() {
        // Given
        TaskRequest request = new TaskRequest(
                "New Task",
                "Description",
                assigneeId,
                TaskPriority.CRITICAL,
                180
        );

        when(userRepository.findById(assigneeId)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        TaskResponse response = taskUseCase.create(request, creator);

        // Then
        assertThat(response).isNotNull();
        verify(userRepository).findById(assigneeId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Should create task with default priority when not specified")
    void shouldCreateTaskWithDefaultPriority() {
        // Given
        TaskRequest request = new TaskRequest("New Task", "Description", null, null, null);
        Task savedTask = Task.builder()
                .id(taskId)
                .title("New Task")
                .description("Description")
                .creator(creator)
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        TaskResponse response = taskUseCase.create(request, creator);

        // Then
        assertThat(response).isNotNull();
        verify(taskRepository).save(any(Task.class));
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Should throw exception when assignee not found")
    void shouldThrowExceptionWhenAssigneeNotFound() {
        // Given
        TaskRequest request = new TaskRequest("Task", "Desc", assigneeId, TaskPriority.HIGH, 60);
        when(userRepository.findById(assigneeId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskUseCase.create(request, creator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assignee not found");

        verify(userRepository).findById(assigneeId);
        verifyNoInteractions(taskRepository);
    }

    @Test
    @DisplayName("Should get all tasks without filters")
    void shouldGetAllTasksWithoutFilters() {
        // Given
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // When
        List<TaskResponse> responses = taskUseCase.getAll(null, null, null);

        // Then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).title()).isEqualTo("Test Task");
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should get tasks filtered by status")
    void shouldGetTasksFilteredByStatus() {
        // Given
        when(taskRepository.findByStatus(TaskStatus.WAITING)).thenReturn(List.of(task));

        // When
        List<TaskResponse> responses = taskUseCase.getAll(TaskStatus.WAITING, null, null);

        // Then
        assertThat(responses).hasSize(1);
        verify(taskRepository).findByStatus(TaskStatus.WAITING);
    }

    @Test
    @DisplayName("Should get tasks filtered by status and assignee")
    void shouldGetTasksFilteredByStatusAndAssignee() {
        // Given
        when(taskRepository.findByStatusAndAssigneeId(TaskStatus.WAITING, assigneeId))
                .thenReturn(List.of(task));

        // When
        List<TaskResponse> responses = taskUseCase.getAll(TaskStatus.WAITING, null, assigneeId);

        // Then
        assertThat(responses).hasSize(1);
        verify(taskRepository).findByStatusAndAssigneeId(TaskStatus.WAITING, assigneeId);
    }

    @Test
    @DisplayName("Should get tasks filtered by priority")
    void shouldGetTasksFilteredByPriority() {
        // Given
        when(taskRepository.findByPriority(TaskPriority.HIGH)).thenReturn(List.of(task));

        // When
        List<TaskResponse> responses = taskUseCase.getAll(null, TaskPriority.HIGH, null);

        // Then
        assertThat(responses).hasSize(1);
        verify(taskRepository).findByPriority(TaskPriority.HIGH);
    }

    @Test
    @DisplayName("Should get tasks filtered by assignee")
    void shouldGetTasksFilteredByAssignee() {
        // Given
        when(taskRepository.findByAssigneeId(assigneeId)).thenReturn(List.of(task));

        // When
        List<TaskResponse> responses = taskUseCase.getAll(null, null, assigneeId);

        // Then
        assertThat(responses).hasSize(1);
        verify(taskRepository).findByAssigneeId(assigneeId);
    }

    @Test
    @DisplayName("Should get task by id")
    void shouldGetTaskById() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        TaskResponse response = taskUseCase.getById(taskId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(taskId);
        assertThat(response.title()).isEqualTo("Test Task");
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should throw exception when task not found by id")
    void shouldThrowExceptionWhenTaskNotFoundById() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskUseCase.getById(taskId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should update task")
    void shouldUpdateTask() {
        // Given
        TaskRequest request = new TaskRequest(
                "Updated Title",
                "Updated Description",
                assigneeId,
                TaskPriority.LOW,
                240
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(assigneeId)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        TaskResponse response = taskUseCase.update(taskId, request);

        // Then
        assertThat(response).isNotNull();
        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(assigneeId);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent task")
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        // Given
        TaskRequest request = new TaskRequest("Title", "Desc", null, TaskPriority.HIGH, 60);
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskUseCase.update(taskId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete task")
    void shouldDeleteTask() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // When
        taskUseCase.delete(taskId);

        // Then
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent task")
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> taskUseCase.delete(taskId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found");

        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should change task status")
    void shouldChangeTaskStatus() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        TaskResponse response = taskUseCase.changeStatus(taskId, TaskStatus.DONE);

        // Then
        assertThat(response).isNotNull();
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw exception when changing status of non-existent task")
    void shouldThrowExceptionWhenChangingStatusOfNonExistentTask() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskUseCase.changeStatus(taskId, TaskStatus.DONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should log actual time")
    void shouldLogActualTime() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        TaskResponse response = taskUseCase.logActualTime(taskId, 180);

        // Then
        assertThat(response).isNotNull();
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw exception when logging time for non-existent task")
    void shouldThrowExceptionWhenLoggingTimeForNonExistentTask() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskUseCase.logActualTime(taskId, 180))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any());
    }
}
