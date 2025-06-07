package fun.icystal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ZhiHuEmail {

    private String title;

    private List<ZhiHuHotItem> items;
}
