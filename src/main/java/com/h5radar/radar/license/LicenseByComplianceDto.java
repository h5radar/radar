package com.h5radar.radar.license;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"compliance_id", "title", "count" })
public record LicenseByComplianceDto(

    @JsonProperty("compliance_id")
    Long complianceId,

    String title,

    long count
) {}
