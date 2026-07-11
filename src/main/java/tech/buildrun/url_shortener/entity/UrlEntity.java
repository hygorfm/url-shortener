package tech.buildrun.url_shortener.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "urls")
public class UrlEntity {

    @Id
    private String id;

    private String fullUrl;

    private LocalDateTime expiresAt;

}
