package fun.icystal.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ZhiHuQuestion {

    private Long id;

    private String title;

    private String text;

    private List<String> topics;

    private LocalDateTime fetchTime;

    private List<LocalDateTime> rankTime;

    private String url;
}
