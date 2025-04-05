package EkraniBackend.movie_platform.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import EkraniBackend.movie_platform.model.ErrorResponse;
import EkraniBackend.movie_platform.model.Media;
import EkraniBackend.movie_platform.service.MediaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @GetMapping
    public ResponseEntity<List<Media>> getAllMedia() {
        return ResponseEntity.ok(mediaService.getAllMedia());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createMedia(@RequestBody Media media) {
        if (media.getTitle() == null || media.getGenres() == null || media.getYear() <= 0 || media.getPrice() == null || media.getType() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Missing required fields"));
        }

        try {
            Media createdMedia = mediaService.createMedia(media);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMedia);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating media", "error", ex.getMessage()));
        }
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Media>> getMovies(@RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean featuredType) {
        return ResponseEntity.ok(mediaService.getMovies(title, featuredType));
    }

    @GetMapping("/tvshows")
    public ResponseEntity<List<Media>> getTvShows(@RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean featuredType) {
        return ResponseEntity.ok(mediaService.getTvShows(title, featuredType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMediaById(@PathVariable String id) {
        Optional<Media> media = mediaService.getMediaById(id);
        if (media.isPresent()) {
            return ResponseEntity.ok(media.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Media not found with ID", id));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedia(@PathVariable String id, @RequestBody Media media) {
        Optional<Media> existingMedia = mediaService.getMediaById(id);
        if (!existingMedia.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Media not found with ID", "id", id));
        }

        if (media.getTitle() == null || media.getGenres() == null || media.getYear() <= 0 || media.getPrice() == null || media.getType() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Missing required fields"));
        }

        media.setId(new ObjectId(id));

        try {
            Media updatedMedia = mediaService.updateMedia(media);
            return ResponseEntity.ok(updatedMedia);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error updating media", "error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedia(@PathVariable String id) {
        System.out.println("Attempting to delete media with ID: " + id);
        Optional<Media> existingMedia = mediaService.getMediaById(id);
        if (!existingMedia.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Media not found with ID: " + id);
        }

        try {
            mediaService.deleteMedia(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Media with ID " + id + " successfully deleted.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting media with ID: " + id);
        }
    }

}
