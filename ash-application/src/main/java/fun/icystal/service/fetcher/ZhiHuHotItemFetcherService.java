package fun.icystal.service.fetcher;

import fun.icystal.fetcher.thief.Thief;
import fun.icystal.mapper.ZhiHuHotItemMapper;
import fun.icystal.mapper.ZhiHuQuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URI;


@Service
@Slf4j
@RequiredArgsConstructor
public class ZhiHuHotItemFetcherService {

    private final ZhiHuHotItemMapper zhiHuHotItemMapper;

    private final ZhiHuQuestionMapper zhiHuQuestionMapper;

    public void executeFetcherTask() {
        Document response = Thief.execute(target, 10);



    }

    private static final URI target =  URI.create("https://www.zhihu.com/hot");

}
