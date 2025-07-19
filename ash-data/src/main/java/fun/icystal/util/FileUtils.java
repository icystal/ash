package fun.icystal.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
public class FileUtils {

    public static final int TIKA_DETECT_MARK_READ_LIMIT = 1024 * 1024;

    private static final Tika tika = new Tika();

    public static String detectFileType(InputStream inputStream) {
        try {
            if (!inputStream.markSupported()) {
                throw new IllegalArgumentException("输入流必须支持 mark 和 reset");
            }
            inputStream.mark(TIKA_DETECT_MARK_READ_LIMIT);
            return tika.detect(inputStream);
        } catch (Exception e) {
            log.error("判断文件类型失败", e);
            return "";
        } finally {
            try {
                inputStream.reset();
            } catch (Exception ignore) {}
        }
    }

    public static String getFileExtension(String mimeType) {
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType type = allTypes.forName(mimeType);
            return type.getExtension();
        } catch (MimeTypeException e) {
            log.error("判断文件扩展名失败", e);
            return "";
        }
    }

    public static String createFileName(InputStream inputStream) {
        return UUID.randomUUID() + getFileExtension(detectFileType(inputStream));
    }
}
