package com.raved.social;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class ContextLoadsTest {

    @Test
    void contextLoads() {
    }
}

