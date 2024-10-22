package com.emse.spring.automacorp.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DummyUserService implements UserService{

    private GreetingService greetingService;

    @Autowired
    public DummyUserService(GreetingService greetingService){this.greetingService = greetingService;}

    @Override
    public void greetAll(List<String> name){
        for (String S:name){
            greetingService.greet(S);
        }
    }

}
