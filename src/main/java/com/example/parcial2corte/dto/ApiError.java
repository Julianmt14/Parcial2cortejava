package com.example.parcial2corte.dto;

import java.time.Instant;
import java.util.List;

public record ApiError(
        String message,
        int status,
        String path,
        Instant timestamp,
        List<String> details
) {}
