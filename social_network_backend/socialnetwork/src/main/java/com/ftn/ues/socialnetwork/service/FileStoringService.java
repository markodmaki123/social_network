package com.ftn.ues.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface FileStoringService {

    public void uploadFile(ByteArrayOutputStream outputStream, String filename, String contentType);

    public InputStream getFile(String objectName);
}
