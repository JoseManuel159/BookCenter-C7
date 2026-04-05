package com.example.imagenes.service.serviceImpl;
import com.example.imagenes.model.ImageMetadata;
import com.example.imagenes.repository.ImageRepository;
import com.example.imagenes.service.ImageService;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private ImageRepository imageRepository;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public String uploadAndProcess(MultipartFile file) throws Exception {
        // CORRECCIÓN DEL ERROR: Usamos .bytes() para obtener el array directamente
        byte[] webpData = ImmutableImage.loader()
                .fromStream(file.getInputStream())
                .scaleToWidth(400) // Redimensionar a 400px
                .bytes(WebpWriter.DEFAULT); // Convertir a WebP en memoria

        // Nombre único con carpeta 'uploads/' como pide el requerimiento
        String fileName = "uploads/" + UUID.randomUUID() + ".webp";

        // Subir a MinIO
        try (InputStream is = new ByteArrayInputStream(webpData)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(is, webpData.length, -1)
                            .contentType("image/webp")
                            .build()
            );
        }

        // Guardar metadatos en PostgreSQL
        ImageMetadata metadata = new ImageMetadata();
        metadata.setFileName(fileName);
        metadata.setOriginalName(file.getOriginalFilename());
        metadata.setSize((long) webpData.length);
        metadata.setFileUrl(fileName); // O la URL completa si prefieres
        imageRepository.save(metadata);

        return "Éxito: Imagen guardada en MinIO como " + fileName;
    }

    @Override
    public List<ImageMetadata> getAllImages() {
        return imageRepository.findAll();
    }
}