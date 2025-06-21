package fun.icystal.act.config;

import fun.icystal.context.AshContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
@Slf4j
public class OverviewTemplate {

    private String title;

    private Method processor;

    private Object bean;

    public Object process() {
        try {
            return processor.invoke(bean);
        } catch (Exception e) {
            log.error("调用 {} Overview Processor 方法异常, ", title, e);
            return null;
        }
    }

    public Object process(AshContext context) {
        try {
            return processor.invoke(bean, context);
        } catch (Exception e) {
            log.error("调用 {} Overview Processor 方法异常, ", title, e);
            return null;
        }
    }
}
