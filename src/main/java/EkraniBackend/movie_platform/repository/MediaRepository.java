package EkraniBackend.movie_platform.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import EkraniBackend.movie_platform.model.Media;

import java.util.List;

public interface MediaRepository extends MongoRepository<Media, String> {
    List<Media> findByType(String type);

    List<Media> findByTypeAndTitleAndFeaturedType(String type, String title, Boolean featuredType);

    List<Media> findByTypeAndTitle(String type, String title);

    List<Media> findByTypeAndFeaturedType(String type, Boolean featuredType);

}
