package fun.icystal.act.service;

import fun.icystal.act.mapper.CaiLianHeadLineMapper;
import fun.icystal.entity.CaiLianHeadline;
import fun.icystal.vo.CaiLianHeadlineVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaiLianService {

    private final CaiLianHeadLineMapper caiLianHeadLineMapper;

    public void saveHeadline(List<CaiLianHeadline> caiLianHeadlines) {
        Integer cnt = caiLianHeadLineMapper.insert(caiLianHeadlines);
        log.info("[财联社] saved {} headline items", cnt);
    }

    public List<CaiLianHeadlineVO> headlinesYesterday() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.plusDays(-1);
        List<CaiLianHeadline> caiLianHeadlines = caiLianHeadLineMapper.selectByDuration(startTime, endTime);

        Set<String> linkSet = new HashSet<>();
        return caiLianHeadlines.stream()
                .filter(headline -> Objects.nonNull(headline)
                        && headline.getTitle() != null && !headline.getTitle().isBlank()
                        && headline.getLink() != null && !headline.getLink().isBlank())
                .filter(headline -> linkSet.add(headline.getLink()))
                .map(headline -> {
                    CaiLianHeadlineVO headlineVO = new CaiLianHeadlineVO();
                    headlineVO.setTitle(headline.getTitle());
                    headlineVO.setLink(headline.getLink());
                    headlineVO.setBrief(headline.getBrief());
                    headlineVO.setPublicationTime(headline.getPublicationTime());
                    return headlineVO;
                })
                .sorted((o1, o2) -> o1.getPublicationTime().isBefore(o2.getPublicationTime()) ? 1 : -1)
                .toList();
    }

}
