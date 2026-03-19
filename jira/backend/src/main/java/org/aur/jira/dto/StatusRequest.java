package org.aur.jira.dto;

import jakarta.validation.constraints.NotNull;
import org.aur.jira.domain.enums.TaskStatus;

public record StatusRequest(@NotNull TaskStatus status) {}
