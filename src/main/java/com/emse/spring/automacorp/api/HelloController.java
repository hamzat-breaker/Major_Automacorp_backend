package com.emse.spring.automacorp.api;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
@Transactional
public class HelloController {
    @GetMapping("/{name}")
    public Message welcome(@PathVariable String name) {
        return new Message("Hello " + name);
    }

    public record Message(String message) {
    }
}