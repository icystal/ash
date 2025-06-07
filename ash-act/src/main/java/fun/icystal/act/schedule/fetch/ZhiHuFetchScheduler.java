package fun.icystal.act.schedule.fetch;

import fun.icystal.entity.ZhiHuHotItem;
import fun.icystal.act.service.ZhiHuService;
import fun.icystal.ash.crawler.fetcher.ZhiHuHotItemFetcher;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ZhiHuFetchScheduler {

    @Resource
    private ZhiHuService zhiHuService;

    private ZhiHuHotItemFetcher zhiHuHotItemFetcher;

    @PostConstruct
    public void initScheduler() {
        zhiHuHotItemFetcher = new ZhiHuHotItemFetcher();
    }

    @Scheduled(cron = "7 30 * * * ?")
    public void fetchHotItems() {
        log.info("[知乎] schedule task for hot items start");
        List<ZhiHuHotItem> items = zhiHuHotItemFetcher.fetch(3);
        zhiHuService.saveHotItems(items);
    }


}
