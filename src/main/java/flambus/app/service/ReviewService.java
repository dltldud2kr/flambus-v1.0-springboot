package flambus.app.service;


import flambus.app.entity.ReviewTagType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ReviewService {
    void createReview();

    long getTotalReviewCount(long storeIdx);

    long getRepresentReivew(long storeIdx);

    ReviewTagType getReivewTypeByIdx(long idx);



}
