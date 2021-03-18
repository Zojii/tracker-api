package us.rise8.tracker;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.junit.jupiter.api.Test;

import us.rise8.tracker.config.Startup;

@SpringBootTest
class trackerApiTests {

    @MockBean
    Startup startup;

    @Test
    void contextLoads() {
    }

}
