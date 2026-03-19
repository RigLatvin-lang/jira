package org.aur.jira.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualTimeRequest(@NotNull @Min(0) Integer minutes) {}
