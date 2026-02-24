package com.example.appspringdata;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppSpringDataApplicationTests {

    @Test
    void contextLoads() {
    }

    // tag::verify[]
    final Driver driver;
    public AppSpringDataApplicationTests(@Autowired Driver driver) {
        this.driver = driver;
    }

    @Test
    final void testConnection() {
        driver.verifyConnectivity();
    }
    // end::verify[]
}
