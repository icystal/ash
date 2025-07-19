package fun.icystal.minio;

import fun.icystal.util.FileUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioManager {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucket;

    /**
     * 从 输入流 上传文件到默认的 bucket
     * @param inputStream 输入流
     * @return 上传成功返回 文件名, 否则返回 null
     */
    public String uploadFile(InputStream inputStream) {
        try {
            return uploadFile(inputStream, bucket);
        } catch (Exception e) {
            log.error("保存文件到 minio 失败", e);
            return null;
        }
    }

    /**
     * 从 输入流 上传文件到指定的 bucket
     * @param inputStream 输入流
     * @param name bucket name
     * @return 上传成功返回 文件名, 否则返回 null
     */
    public String uploadFile(InputStream inputStream, String name) throws IOException {
        if (inputStream == null) {
            return null;
        }
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream);
        }
        try {
            String fileName = FileUtils.createFileName(inputStream);
            minioClient.putObject(PutObjectArgs.builder()
                            .bucket(name)
                            .object(fileName)
                            .stream(inputStream, -1, 1024 * 1024 * 1024)
                            .build());
            return fileName;
        } catch (Exception e) {
            log.error("上传文件到 minio 失败", e);
            return null;
        } finally {
            inputStream.close();
        }
    }

    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("从 minio 下载文件失败", e);
            return null;
        }
    }

    /**
     * 获取文件 url
     * @param bucketName bucket name
     * @param objectName 对象名称
     * @param expires 过期时间 (天)
     * @return 文件 url
     */
    public String getObjectUrl(String bucketName, String objectName, int expires) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expires, TimeUnit.DAYS)
                    .build());
        } catch (Exception e) {
            log.error("从 minio 获取文件失败", e);
            return null;
        }
    }

    /**
     * 从默认 bucket 获取文件 url, 默认过期时间为 7天
     * @param objectName 对象名称
     * @return 文件 url
     */
    public String getObjectUrl(String objectName) {
        return getObjectUrl(bucket, objectName, 7);
    }

    /**
     * 创建一个 minio bucket
     * @param name bucket name
     * @return 如果 bucket 已经存在, 返回 false; 否则, 创建 bucket, 并返回 true
     */
    public boolean createBucket(String name) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (exists) {
                return false;
            }
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("创建 minio bucket 失败");
        }
    }

    /**
     * 从默认 bucket 删除文件
     * @param objectName 目标文件
     */
    public void deleteFile(String objectName) {
        deleteFile(bucket, objectName);
    }

    /**
     * 删除文件
     * @param bucketName bucket name
     * @param objectName 目标文件
     */
    public void deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("从 minio 删除文件失败", e);
        }
    }

    /**
     * 查询所有 minio bucket 列表
     * @return buckets
     */
    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException("查询 minio bucket 失败");
        }
    }

    /**
     * 删除 minio bucket
     * @param name bucket name
     */
    public void removeBucket(String name) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build());
        } catch (Exception e) {
            throw new RuntimeException("删除 minio bucket 失败");
        }
    }

}
