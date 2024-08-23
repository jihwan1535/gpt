package com.test.openai.image;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Component
public class S3ImageUploader {

    private final S3Client amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(final byte[] imageData, String contentType) {
        final String saveFileName = parseSaveFileName();
        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(saveFileName)
                .contentType(contentType)
                .build();

        amazonS3.putObject(putObjectRequest,
                RequestBody.fromBytes(imageData));

        return amazonS3.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(saveFileName))
                .toString();
    }

    private String parseSaveFileName() {
        final String fileBaseName = UUID.randomUUID().toString().substring(0, 8);
        return fileBaseName + "_" + System.currentTimeMillis() + ".png";
    }

}