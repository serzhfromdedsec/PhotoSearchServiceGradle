package com.example.photoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PhotoSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoSearchApplication.class, args);
    }
}

@RestController
@RequestMapping("/photos")
class PhotoController {

    private final Map<String, String> photoStorage = new HashMap<>();

    public PhotoController() {
        photoStorage.put("thesound", "photos/thesound.png");
        photoStorage.put("sons", "photos/sons.jpg");
        photoStorage.put("peace", "photos/peace.jpg");
    }

    @GetMapping("/{title}")
    public ResponseEntity<byte[]> getPhotoByTitle(@PathVariable String title) {
        String filePath = photoStorage.get(title);
        if (filePath == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            Path path = Paths.get(filePath);
            byte[] photoBytes = Files.readAllBytes(path);
            String contentType = Files.probeContentType(path);

            return ResponseEntity
                    .ok()
                    .header("Content-Type", contentType)
                    .body(photoBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
