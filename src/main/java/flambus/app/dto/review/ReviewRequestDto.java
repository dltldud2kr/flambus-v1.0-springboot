package flambus.app.dto.review;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Builder
@Data
@AllArgsConstructor
public class ReviewRequestDto {
    private long memberIdx;
    private long storeIdx;
    private long tagIdx;
    private String content;
    private List<MultipartFile> reviewImage;

}
