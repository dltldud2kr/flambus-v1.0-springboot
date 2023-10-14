package flambus.app.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class FileResponse {
    @Builder
    @Data
    @AllArgsConstructor
    public static class ReviewImageDto {
        private String fileName;
        private Long fileSize;
        private String imageUrl;

    }

}
