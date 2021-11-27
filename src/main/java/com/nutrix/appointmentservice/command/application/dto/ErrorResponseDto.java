package com.nutrix.appointmentservice.command.application.dto;

import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private final String message;
    public ErrorResponseDto(String message)
    {
        this.message = message;
    }
}
