package fun.icystal.service;

import fun.icystal.minio.MinioManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final MinioManager minioManager;

    public String saveImage(InputStream inputStream) {
        try {
            return minioManager.uploadFile(inputStream);
        } catch (Exception e) {
            log.error("保存图片失败", e);
        }
        return null;
    }

    public void deleteImage(String fileName) {
        minioManager.deleteFile(fileName);
    }

    public String queryImage(String fileName) {
        return minioManager.getObjectUrl(fileName);
    }

}
