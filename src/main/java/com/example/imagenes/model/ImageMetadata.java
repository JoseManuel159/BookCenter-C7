package com.example.imagenes.model;

import jakarta.persistence.*;
        import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "images_metadata")
@Data
public class ImageMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;    // Nombre en MinIO
    private String originalName;
    private String fileUrl;     // Ruta dentro del bucket
    private Long size;
    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
}