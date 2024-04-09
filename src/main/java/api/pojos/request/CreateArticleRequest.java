package api.pojos.request;

import lombok.*;

import java.time.Instant;


@Data
@Builder
public class CreateArticleRequest {

    private String title;
    private String logo;
    private String description;
    private String content;
    private Boolean visible;
    private Instant publishDate;
    private Long authorId;
}
