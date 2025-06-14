package fun.icystal.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class CaiLianHeadlineVO {

    private String title;

    private String link;

    private String brief;

    private LocalDateTime publicationTime;
}
