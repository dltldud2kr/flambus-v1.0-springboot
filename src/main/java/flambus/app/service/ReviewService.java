package flambus.app.service;


import flambus.app.dto.review.ReviewRequestDto;
import flambus.app.entity.ReviewTagType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public interface ReviewService {
    void createJournal(ReviewRequestDto reviewRequestDto) throws IOException;

    long getTotalReviewCount(long storeIdx);

    long getRepresentReivew(long storeIdx);

    ReviewTagType getReivewTypeByIdx(long idx);



}
