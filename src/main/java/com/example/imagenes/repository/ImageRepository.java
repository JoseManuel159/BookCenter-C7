package com.example.imagenes.repository;

import com.example.imagenes.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageMetadata, Long> {}