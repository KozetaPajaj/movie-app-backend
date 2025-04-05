package EkraniBackend.movie_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EkraniBackend.movie_platform.model.Media;
import EkraniBackend.movie_platform.repository.MediaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;

    public Media createMedia(Media media) {
        return mediaRepository.save(media);
    }

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public List<Media> getMovies(String title, Boolean featuredType) {
        return filterMedia("movie", title, featuredType);
    }

    public List<Media> getTvShows(String title, Boolean featuredType) {
        return filterMedia("tvshow", title, featuredType);
    }

    private List<Media> filterMedia(String type, String title, Boolean featuredType) {
        if (title != null && featuredType != null) {
            return mediaRepository.findByTypeAndTitleAndFeaturedType(type, title, featuredType);
        } else if (title != null) {
            return mediaRepository.findByTypeAndTitle(type, title);
        } else if (featuredType != null) {
            return mediaRepository.findByTypeAndFeaturedType(type, featuredType);
        }
        return mediaRepository.findByType(type);
    }

    public Optional<Media> getMediaById(String id) {
        return mediaRepository.findById(id);
    }

    public Media updateMedia(Media media) {
        if (media.getId() == null || !mediaRepository.existsById(media.getId())) {
            throw new IllegalArgumentException("Invalid media ID");
        }
        return mediaRepository.save(media);
    }

    public void deleteMedia(String id) {
        if (!mediaRepository.existsById(id)) {
            throw new IllegalArgumentException("Media not found with ID: " + id);
        }
        mediaRepository.deleteById(id);
    }
}
