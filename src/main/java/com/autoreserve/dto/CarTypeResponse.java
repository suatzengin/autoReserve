package com.autoreserve.dto;

public record CarTypeResponse(
        String id,
        String name,
        int totalStock
) {}
