package reactdemoapp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactdemoapp.backend.models.CdnContent;
import reactdemoapp.backend.repositories.CdnRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CdnService {
    @Autowired
    CdnRepository cdnRepository;

    @Value("${cdn.path}")
    String cdnPath;

    public CdnContent save(byte[] content) throws IOException {
        String uuid = UUID.randomUUID().toString();

        File file = new File(Paths.get(cdnPath, uuid).toString());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }

        return cdnRepository.save(new CdnContent().setContentPath(uuid));
    }

    public CdnContent get(long contentId) {
        return cdnRepository.findByContentId(contentId);
    }

    public void delete(long id) {
        cdnRepository.deleteById(id);
    }
}
