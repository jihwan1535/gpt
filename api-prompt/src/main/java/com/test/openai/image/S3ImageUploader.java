package com.test.openai.image;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Component
public class S3ImageUploader {

    private final S3Client amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(final MultipartFile multipartFile) {
        final String saveFileName = parseSaveFileName(multipartFile);
        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(saveFileName)
                .contentType(multipartFile.getContentType())
                .build();
        try {
            amazonS3.putObject(putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
        } catch (final IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }

        return amazonS3.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(saveFileName))
                .toString();
    }

    private String parseSaveFileName(final MultipartFile image) {
        final String imageName = image.getOriginalFilename();
        final String extension = StringUtils.getFilenameExtension(imageName);
        final String fileBaseName = UUID.randomUUID().toString().substring(0, 8);
        return fileBaseName + "_" + System.currentTimeMillis() + "." + extension;
    }

}