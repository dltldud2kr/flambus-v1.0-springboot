package flambus.app.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReviewResponseDTO {
 @Builder
 @Data
 @AllArgsConstructor
 public static class Review {
  private Long idx;//리뷰 idx
  private Long memberIdx;//리뷰 작성자 idx
  private String content; //리뷰내용
  private List<ReviewTag> jounalTag;
  private List<Map<String, Object>> jounalImage;
  private LocalDateTime created; //작성 시간
  private LocalDateTime modified; //수정 시간
 }

 @Builder
 @Data
 @AllArgsConstructor
 public static class ReviewTag {
  private Long tagIdx;
  private String tagName;

 }



}
