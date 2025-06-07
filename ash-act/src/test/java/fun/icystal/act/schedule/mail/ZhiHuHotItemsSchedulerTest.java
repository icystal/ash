package fun.icystal.act.schedule.mail;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ZhiHuHotItemsSchedulerTest {

    @Resource
    private ZhiHuHotItemsScheduler zhiHuHotItemsScheduler;

    @Test
    public void hotItemsEmailTest() {
        zhiHuHotItemsScheduler.hotItemsEmail();
    }
}
