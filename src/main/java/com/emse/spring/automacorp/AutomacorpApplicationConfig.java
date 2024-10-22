package com.emse.spring.automacorp;

import com.emse.spring.automacorp.hello.GreetingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutomacorpApplicationConfig {

    @Bean
    public CommandLineRunner greetingCommandLine(GreetingService greetingService){
        return args -> {
            greetingService.greet("Spring");
        };
    }
}
