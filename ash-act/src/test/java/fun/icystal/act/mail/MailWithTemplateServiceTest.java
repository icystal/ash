package fun.icystal.act.mail;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MailWithTemplateServiceTest {
    @Resource
    private MailWithTemplateService mailWithTemplateService;

    @Test
    public void test() {
        // 发送欢迎邮件
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("title", "测试");
        ctx.put("message", "这是一条测试邮件");
        mailWithTemplateService.send(new String[]{"xxx@qq.com"}, "TEST MAIL", "mail/TestMail", ctx);
    }
}
