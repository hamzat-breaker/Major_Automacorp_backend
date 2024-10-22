package com.emse.spring.automacorp.gouv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestClientTest(AddressSearchService.class) // (1)
class AddressSearchServiceTest {
    @Autowired
    private AddressSearchService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockRestServiceServer server; // (2)

    @Test
    void shouldFindAddresses() throws JsonProcessingException {
        // Arrange
        ApiGouvResponse expectedResponse = simulateApiResponse();

        String expectedUrl = UriComponentsBuilder
                .fromUriString("/search")
                .queryParam("q", "cours+fauriel")
                .queryParam("limit", 15)
                .build()
                .toUriString();

        this.server
                .expect(MockRestRequestMatchers.requestTo(expectedUrl))
                .andRespond(
                        MockRestResponseCreators.withSuccess(
                                objectMapper.writeValueAsString(expectedResponse),
                                MediaType.APPLICATION_JSON
                        )
                );

        // Act
        List<ApiGouvAdress> addresses = this.service.searchAddress(List.of("cours", "fauriel"));

        // Assert
        Assertions
                .assertThat(addresses)
                .hasSize(1)
                .extracting(ApiGouvAdress::city)
                .contains("Saint Etienne");
    }

    private ApiGouvResponse simulateApiResponse() {
        ApiGouvAdress address = new ApiGouvAdress(
                "ad1",
                "Cours Fauriel 42100 Saint-&#xC9;tienne",
                "2",
                0.98,
                "42100",
                "42218",
                "Saint Etienne",
                "context",
                "type",
                0.0,
                0.0
        );

        ApiGouvFeature feature = new ApiGouvFeature("type", address);
        return new ApiGouvResponse("v1", "cours+fauriel", 15, List.of(feature));
    }
}