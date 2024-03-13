package gui.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    private Long id;
    private String title;
    private String logo;
    private String description;
    private String content;
    private Boolean visible;
    private String author;
    private String publishDate;
    private String createdDate;
    private String lastUpdateDate;
}
