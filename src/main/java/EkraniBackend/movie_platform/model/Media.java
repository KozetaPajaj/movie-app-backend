package EkraniBackend.movie_platform.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Document(collection = "media")
public class Media {
    @Id
    private ObjectId id;
    private String title;
    private List<String> genres;
    private int year;
    private String imageURL;
    private String description;
    private String duration;
    private String trailer;
    private String type;
    private Boolean featuredType;
    private Price price;

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

}
