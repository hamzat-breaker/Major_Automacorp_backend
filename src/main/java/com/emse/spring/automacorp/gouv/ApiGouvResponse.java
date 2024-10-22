package com.emse.spring.automacorp.gouv;

import java.util.List;

public record ApiGouvResponse(
        String version,
        String query,
        Integer limit,
        List<ApiGouvFeature> features
) {

}
