package fun.icystal.act.service;

import fun.icystal.act.mapper.SubscribeInfoMapper;
import fun.icystal.constant.SubscribeType;
import fun.icystal.entity.SubscribeInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SubscribeService {

    @Resource
    private SubscribeInfoMapper subscribeInfoMapper;

    public List<String> querySubscribedEmail(SubscribeType type) {
        List<SubscribeInfo> subscribeInfos = subscribeInfoMapper.queryByType(type.getType());
        if (subscribeInfos == null) {
            return Collections.emptyList();
        }
        return subscribeInfos.stream()
                .map(SubscribeInfo::getEmail)
                .filter(Objects::nonNull)
                .toList();
    }
}
