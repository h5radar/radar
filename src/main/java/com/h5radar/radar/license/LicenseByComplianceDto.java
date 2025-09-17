package com.h5radar.radar.license;

public record LicenseByComplianceDto(
    Long complianceId,
    String title,
    long count
) {}
