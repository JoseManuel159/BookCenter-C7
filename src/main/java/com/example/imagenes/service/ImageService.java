package com.example.imagenes.service;

import com.example.imagenes.model.ImageMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    String uploadAndProcess(MultipartFile file) throws Exception;
    List<ImageMetadata> getAllImages();
}