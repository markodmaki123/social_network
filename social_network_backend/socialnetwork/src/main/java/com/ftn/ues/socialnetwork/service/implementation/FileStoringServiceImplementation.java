package com.ftn.ues.socialnetwork.service.implementation;


import com.ftn.ues.socialnetwork.service.FileStoringService;
import io.minio.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileStoringServiceImplementation implements FileStoringService {

    @Value("${minio.bucket}")
    String bucketName;

    final MinioClient minioClient;

    @Autowired
    public FileStoringServiceImplementation(final MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void uploadFile(ByteArrayOutputStream outputStream, String filename, String contentType) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .stream(inputStream, outputStream.size(), -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload PDF to MinIO", e);
        }
    }

    @Override
    public InputStream getFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file from MinIO", e);
        }
    }
}
