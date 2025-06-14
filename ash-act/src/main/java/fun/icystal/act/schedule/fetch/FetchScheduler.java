package fun.icystal.act.schedule.fetch;

import fun.icystal.act.service.CaiLianService;
import fun.icystal.ash.crawler.fetcher.CaiLianHeadlineFetcher;
import fun.icystal.entity.CaiLianHeadline;
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
public class FetchScheduler {

    @Resource
    private ZhiHuService zhiHuService;

    private ZhiHuHotItemFetcher zhiHuHotItemFetcher;

    @Resource
    private CaiLianService caiLianService;

    private CaiLianHeadlineFetcher caiLianHeadlineFetcher;

    @PostConstruct
    public void initScheduler() {
        zhiHuHotItemFetcher = new ZhiHuHotItemFetcher();
        caiLianHeadlineFetcher = new CaiLianHeadlineFetcher();
    }

    @Scheduled(cron = "7 48 * * * ?")
    public void fetchHotItems() {
        log.info("[知乎] schedule task for hot items start");
        List<ZhiHuHotItem> items = zhiHuHotItemFetcher.fetch(3);
        zhiHuService.saveHotItems(items);
    }

    @Scheduled(cron = "7 11 * * * ?")
    public void fetchCaiLianHeadline() {
        log.info("[财联社] schedule task for headlines start");
        List<CaiLianHeadline> headlines = caiLianHeadlineFetcher.fetch(3);
        caiLianService.saveHeadline(headlines);
    }


}
