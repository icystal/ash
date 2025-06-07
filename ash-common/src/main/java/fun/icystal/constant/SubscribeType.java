package fun.icystal.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubscribeType {

    ZHI_HU_HOT_ITEMS(1, "知乎热榜");

    private final Integer type;

    private final String desc;
}
