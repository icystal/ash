package fun.icystal.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscribeInfo {

    private String email;

    private Integer subscribeType;

}
