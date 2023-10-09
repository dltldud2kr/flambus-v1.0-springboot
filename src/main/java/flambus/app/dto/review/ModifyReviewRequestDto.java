package flambus.app.dto.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
public class ModifyReviewRequestDto {
    private long reviewIdx;
    private long memberIdx;
    private long storeIdx;
    private long tagIdx;
    private String content;
    private List<MultipartFile> reviewImage;

}
