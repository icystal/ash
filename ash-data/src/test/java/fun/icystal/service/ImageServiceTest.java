package fun.icystal.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SpringBootTest
public class ImageServiceTest {

    @Resource
    private ImageService imageService;

    @Test
    public void downloadAndDeleteImage() throws IOException {
        String url = "https://pic1.zhimg.com/50/v2-93c58c4cda76fc90d97685a341ec05ff_720w.jpg?source=1def8aca";
        InputStream inputStream = new URL(url).openConnection().getInputStream();
        String path = imageService.saveImage(inputStream);
        assert path != null && !path.isBlank();
        System.out.println(path);

        String imgUrl = imageService.queryImage(path);
        assert imgUrl != null && !imgUrl.isBlank();
        System.out.println(imgUrl);

        imageService.deleteImage(path);
    }

}
