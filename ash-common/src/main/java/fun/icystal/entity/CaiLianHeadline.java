package fun.icystal.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CaiLianHeadline {

    private String title;

    private String link;

    private String brief;

    private LocalDateTime publicationTime;
}
