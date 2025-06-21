package fun.icystal.act.controller;

import com.alibaba.fastjson2.JSON;
import fun.icystal.act.config.OverviewTemplate;
import fun.icystal.act.processor.OverviewProcessor;
import fun.icystal.vo.OverviewPageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/overview")
@Slf4j
@Controller
public class OverviewController implements CommandLineRunner {

    @Resource
    private ApplicationContext applicationContext;

    private List<OverviewTemplate> overviewTemplates;

    @GetMapping("/real")
    public String real(Model model) {

        List<OverviewPageVO> pageVOList = new ArrayList<>();
        for (OverviewTemplate template : overviewTemplates) {
            Object vo = template.process();
            OverviewPageVO pageVO = new OverviewPageVO(template.getTitle(), vo);
            pageVOList.add(pageVO);
        }
        model.addAllAttributes(pageVOList);
        log.info("[实时热搜] 返回实时热搜数据: {}", JSON.toJSONString(pageVOList));

        return "front/overview.html";
    }

    @Override
    public void run(String... args) throws Exception {
        ArrayList<OverviewTemplate> templates = new ArrayList<>();

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames){
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = bean.getClass();

            for (Method method : beanClass.getDeclaredMethods()){
                if (method.isAnnotationPresent(OverviewProcessor.class)) {
                    OverviewTemplate template = new OverviewTemplate();
                    template.setBean(bean);
                    template.setProcessor(method);

                    OverviewProcessor annotation = method.getAnnotation(OverviewProcessor.class);
                    template.setTitle(annotation.title());

                    log.error("[OverviewProcessor 初始化] 发现 {} Processor", annotation.title());
                    templates.add(template);
                }
            }
        }
        overviewTemplates = templates;
    }
}
