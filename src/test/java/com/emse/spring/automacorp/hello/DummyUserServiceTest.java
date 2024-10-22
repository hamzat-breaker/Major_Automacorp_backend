package com.emse.spring.automacorp.hello;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(SpringExtension.class)
class DummyUserServiceTest {

    @Configuration
    @ComponentScan("com.emse.spring.automacorp.hello")
    public static class DummyUserServiceTestConfig{}

    @Autowired
    public DummyUserService dummyUserService;

    @Test
    public void testGreetingAll(CapturedOutput output) {
        dummyUserService.greetAll(List.of("Elodie", "Charles"));
        Assertions.assertThat(output).contains("Hello, Elodie!", "Hello, Charles!");
    }
}
