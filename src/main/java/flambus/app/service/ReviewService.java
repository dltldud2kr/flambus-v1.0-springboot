package flambus.app.service;


import flambus.app.dto.review.CreateReviewRequestDto;
import flambus.app.dto.review.ModifyReviewRequestDto;
import flambus.app.dto.store.StoreJounalDto;
import flambus.app.entity.ReviewTagType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface ReviewService {
    void createJournal(CreateReviewRequestDto createReviewRequestDto) throws IOException;

    void updateJournal(ModifyReviewRequestDto modifyReviewRequestDto);

    long getTotalReviewCount(long storeIdx);

    long getRepresentReivewIdx(long storeIdx);

    List<StoreJounalDto> getStoreJounalList(long storeIdx, int pageNum, int pageSize);

    ReviewTagType getReivewTypeByIdx(long idx);



}
