package fun.icystal.act.processor;

import fun.icystal.act.config.OverviewTemplate;
import fun.icystal.act.controller.OverviewController;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OverviewProcessorTest {

    @Resource
    private OverviewController overviewController;

    @Test
    public void OverviewProcessor() {
        overviewController.real(null);
    }

}
