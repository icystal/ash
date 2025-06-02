package fun.icystal.act;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ActApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActApplication.class, args);
    }
}
