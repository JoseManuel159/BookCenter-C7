package com.example.imagenes.controller;

import com.example.imagenes.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String result = imageService.uploadAndProcess(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // AGREGA ESTO:
    @GetMapping("/list")
    public ResponseEntity<?> listImages() {
        try {
            return ResponseEntity.ok(imageService.getAllImages());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al listar: " + e.getMessage());
        }
    }

}