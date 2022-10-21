package reactdemoapp.backend.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import reactdemoapp.backend.payload.response.NewContentResponse;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(schema = "public", name = "cdn_content")
public class CdnContent {
    @Value("${cdn.path}")
    String cdnPath;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private long contentId;
    private String contentPath;

    public byte[] getContent() throws IOException {
        return Files.readAllBytes(Paths.get(cdnPath, contentPath).toAbsolutePath());
    }

    public NewContentResponse toResponse() {
        return new NewContentResponse().setContentId(contentId);
    }
}
