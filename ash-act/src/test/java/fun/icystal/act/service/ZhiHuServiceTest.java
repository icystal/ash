package fun.icystal.act.service;

import fun.icystal.vo.ZhiHuItemVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ZhiHuServiceTest {
    @Resource
    private ZhiHuService zhiHuService;

    @Test
    public void hotItemsYesterdayTest() {
        List<ZhiHuItemVO> itemVOS = zhiHuService.hotItemsYesterday();
        System.out.println(itemVOS);
    }
}
