package com.emse.spring.automacorp.gouv;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class AddressSearchService {

    private final RestTemplate restTemplate;

    public AddressSearchService(RestTemplateBuilder restTemplateBuilder) {
        String apiUrl = "https://adresse.data.gouv.fr";
        this.restTemplate = restTemplateBuilder.rootUri(apiUrl).build();
    }

    public List<ApiGouvAdress> searchAddress(List<String> keys) {
        String params = String.join("+", keys);
        String uri = UriComponentsBuilder.fromUriString("/search")
                .queryParam("q", params)
                .queryParam("limit", 15)
                .build()
                .toUriString();

        ApiGouvResponse apiGouvResponse = restTemplate.getForObject(uri, ApiGouvResponse.class);

        if (apiGouvResponse != null && apiGouvResponse.features() != null) {
            return apiGouvResponse.features().stream()
                    .map(ApiGouvFeature::properties)
                    .toList();
        } else {
            return List.of();
        }
    }
}