package fun.icystal.act.schedule.mail;

import fun.icystal.act.service.SubscribeService;
import fun.icystal.constant.SubscribeType;
import fun.icystal.vo.ZhiHuItemVO;
import fun.icystal.act.mail.MailWithTemplateService;
import fun.icystal.act.service.ZhiHuService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class ZhiHuHotItemsScheduler {

    private static final String title = "知乎 · 昨日热榜";

    private static final String subject = "[ash] 昨日热榜 · 知乎";

    private static final String template = "mail/ZhiHuHotItems.html";

    @Resource
    private ZhiHuService zhiHuService;

    @Resource
    private MailWithTemplateService mailWithTemplateService;

    @Resource
    private SubscribeService subscribeService;

    @Scheduled(cron = "0 32" +
            " * * * ?")
    public void hotItemsEmail() {
        List<String> emails = subscribeService.querySubscribedEmail(SubscribeType.ZHI_HU_HOT_ITEMS);
        if (emails.isEmpty()) {
            return;
        }

        List<ZhiHuItemVO> items = zhiHuService.hotItemsYesterday();

        HashMap<String, Object> mailContent = new HashMap<>();
        mailContent.put("hotItems", items);
        mailContent.put("title", title);

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.plusDays(-1);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        mailContent.put("startTime", timeFormatter.format(startTime));
        mailContent.put("endTime", timeFormatter.format(endTime));

        mailWithTemplateService.send(emails.toArray(new String[0]), subject, template, mailContent);
    }
}
