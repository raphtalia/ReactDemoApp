package reactdemoapp.backend.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class NewContentResponse {
    private long contentId;
}
