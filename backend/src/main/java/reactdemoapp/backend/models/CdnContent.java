package reactdemoapp.backend.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(schema = "public", name = "cdn_content")
public class CdnContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private long contentId;
    private String contentPath;
}
