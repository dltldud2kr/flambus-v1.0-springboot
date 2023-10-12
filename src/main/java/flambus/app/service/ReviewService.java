package flambus.app.service;


import flambus.app.dto.review.ReviewRequest;
import flambus.app.dto.review.ReviewResponse;
import flambus.app.entity.ReviewTagType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ReviewService {

    void createJournal(ReviewRequest.CreateReviewRequestDto request) throws IOException;

    void updateJournal(ReviewRequest.ModifyReviewRequestDto request);

    long getTotalReviewCount(long storeIdx);

    long getRepresentReivewIdx(long storeIdx);

    List<ReviewResponse.StoreJounalDto> getStoreJounalList(Long storeIdx, int pageNum, int pageSize);

    ReviewTagType getReivewTypeByIdx(long idx);



}
