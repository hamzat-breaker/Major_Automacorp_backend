package com.emse.spring.automacorp.gouv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/address")
@Transactional
public class AddressSearchController {
    /**
     * This implementation does work with swagger. Yet, the call "cours - fauriel" doesn't return
     * the wanted value, only a "not found" error is present.
     */

    private final AddressSearchService addressSearchService;

    @Autowired
    public AddressSearchController(AddressSearchService addressSearchService) {
        this.addressSearchService = addressSearchService;
    }

    @GetMapping
    public List<ApiGouvAdress> searchAddresses(@RequestParam List<String> keys) {
        return addressSearchService.searchAddress(keys);
    }
}
