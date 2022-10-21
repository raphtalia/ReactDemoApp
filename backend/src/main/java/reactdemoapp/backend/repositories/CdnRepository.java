package reactdemoapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import reactdemoapp.backend.models.CdnContent;

public interface CdnRepository extends JpaRepository<CdnContent, Long> {
    CdnContent findByContentId(long contentId);
}
