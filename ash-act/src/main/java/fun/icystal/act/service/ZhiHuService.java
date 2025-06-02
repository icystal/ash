package fun.icystal.act.service;

import fun.icystal.ZhiHuHotItem;
import fun.icystal.act.mapper.ZhiHuHotItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZhiHuService {

    private final ZhiHuHotItemMapper zhiHuHotItemMapper;

    public void saveHotItems(List<ZhiHuHotItem> hotItems) {
        Integer cnt = zhiHuHotItemMapper.insert(hotItems);
        log.info("[知乎] saved {} hot items", cnt);





    }

}
