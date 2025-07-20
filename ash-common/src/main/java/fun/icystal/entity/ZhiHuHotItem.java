package fun.icystal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ZhiHuHotItem {

    private Long questionId;

    private LocalDateTime fetchTime;

    private Integer rank;

}
