package com.aviva.enrichment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EnrichmentControllerIntegrationTest{
    @Autowired
    private MockMvc mockMvc;
    @Test
    void shouldRejectHighRiskTransaction() throws Exception {
        String request = "{\"merchantId\":\"MERCH-001\",\"customerId\":\"CUST-001\",\"amount\":75000,\"currency\":\"USD\",\"merchantCategory\":\"CRYPTO\",\"sourceCountry\":\"GB\",\"destinationCountry\":\"KP\",\"channel\":\"ONLINE\"}";

        mockMvc.perform(post("/api/v1/enrichment/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value("REJECTED"))
                .andExpect(jsonPath("$.riskScore").isNumber())
                .andExpect(jsonPath("$.signals").isArray());
    }
    @Test
    void shouldReturn400ForInvalidRequest() throws Exception{
        String request = """
            {
                "merchantId": "MERCH-002",
                "customerId": "CUST-002",
                "amount": 50,
                "currency": "GBP",
                "merchantCategory": "GROCERY",
                "sourceCountry": "GB",
                "destinationCountry": "GB",
                "channel": "POS"
            }
            """;

        mockMvc.perform(post("/api/v1/enrichment/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value("APPROVED"))
                .andExpect(jsonPath("$.riskScore").value(0));
    }
}