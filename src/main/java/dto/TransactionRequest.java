package com.aviva.enrichment.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank String merchantId,
        @NotBlank String customerId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotBlank String merchantCategory,
        @NotBlank String sourceCountry,
        @NotBlank String destinationCountry,
        @NotBlank String channel
) {}