package com.ems;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test: fails fast if the ApplicationContext can't wire up
 * (missing bean, bad config, broken @Autowired, etc.).
 */
@SpringBootTest
class EmployeeManagementApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
