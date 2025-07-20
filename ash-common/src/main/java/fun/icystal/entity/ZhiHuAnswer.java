package fun.icystal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ZhiHuAnswer {

    private Long id;

    private Long questionId;

    private String text;

    private String author;

    private Integer star;

    private Integer comments;

    private LocalDateTime updateTime;

}
