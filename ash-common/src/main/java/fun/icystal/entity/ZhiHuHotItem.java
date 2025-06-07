package fun.icystal.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ZhiHuHotItem {

    private Long id;

    private String title;

    private String link;

    private String excerpt;

    private String imgUrl;

    private Integer sort;

    private LocalDateTime fetchTime;
}
