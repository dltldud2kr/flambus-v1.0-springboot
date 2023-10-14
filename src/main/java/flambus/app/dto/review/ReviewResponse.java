package flambus.app.dto.review;


import flambus.app.dto.upload.FileResponse;
import jdk.vm.ci.meta.Local;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class ReviewResponse {
    @Builder
    @Data
    @AllArgsConstructor
    public static class StoreJounalDto {
        private Long reviewidx;//리뷰 idx
        private Long storeIdx;
        private Long memberIdx;//리뷰 작성자 idx
        private String content; //리뷰내용
        private List<Map<String,Object>> jounalImage;
        private List<Map<String,Object>> tagList;
        private LocalDateTime created; //작성 시간
        private LocalDateTime modified; //수정 시간
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class MostLikeReviewDto {
        private Long reviewIdx;
        private Long storeIdx;
        private Long likeCount;
        private Map<String,Object> creator;
        private FileResponse.ReviewImageDto reviewImage;

    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class JournalTag {
        private Long tagIdx;
        private String tagName;
    }



}
