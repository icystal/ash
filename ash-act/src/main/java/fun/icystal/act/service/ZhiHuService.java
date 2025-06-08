package fun.icystal.act.service;

import fun.icystal.entity.ZhiHuHotItem;
import fun.icystal.vo.ZhiHuItemVO;
import fun.icystal.act.mapper.ZhiHuHotItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZhiHuService {

    private static final Integer maxDisplayHotItemsSize = 99;

    private static final Integer dominateTagThreshold = 12;

    private final ZhiHuHotItemMapper zhiHuHotItemMapper;

    public void saveHotItems(List<ZhiHuHotItem> hotItems) {
        Integer cnt = zhiHuHotItemMapper.insert(hotItems);
        log.info("[知乎] saved {} hot items", cnt);
    }

    public List<ZhiHuItemVO> hotItemsYesterday() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.plusDays(-1);
        List<ZhiHuHotItem> hotItems = zhiHuHotItemMapper.queryByDuration(startTime, endTime);

        Map<String, Integer> cntMap = new HashMap<>();
        List<ZhiHuItemVO> itemVOs = hotItems.stream()
                .filter(item -> Objects.nonNull(item) && Objects.nonNull(item.getLink()) && Objects.nonNull(item.getTitle()))
                .peek(item -> cntMap.merge(item.getLink(), 1, Integer::sum))
                .filter(item -> cntMap.get(item.getLink()) == 1)
                .sorted((o1, o2) -> {
                    if (Objects.equals(cntMap.get(o2.getLink()), cntMap.get(o1.getLink()))) {
                        if (o1.getSort() == null) {
                            return 1;
                        } else if (o2.getSort() == null) {
                            return -1;
                        } else {
                            return o1.getSort() - o2.getSort();
                        }
                    } else {
                        return cntMap.get(o2.getLink()) - cntMap.get(o1.getLink());
                    }
                })
                .map(item -> {
                    ZhiHuItemVO itemVO = new ZhiHuItemVO();
                    itemVO.setTitle(item.getTitle().strip());
                    itemVO.setLink(item.getLink());
                    return itemVO;
                })
                .toList();
        if (itemVOs.size() > maxDisplayHotItemsSize) {
            itemVOs = itemVOs.subList(0, maxDisplayHotItemsSize);
        }
        handleDominateTag(itemVOs, cntMap);
        return itemVOs;
    }

    /**
     * 处理霸榜标签
     * 如果 话题 24h 内出现超过 dominateTagThreshold 次, 打上霸榜标签
     */
    private void handleDominateTag(List<ZhiHuItemVO> zhiHuItemVOs, Map<String, Integer> cntMap) {
        zhiHuItemVOs.forEach(item -> {
            Integer cnt = cntMap.get(item.getLink());
            item.setDominate(cnt != null && cnt >= dominateTagThreshold);
        });
    }



}
