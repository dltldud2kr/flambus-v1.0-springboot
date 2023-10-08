package flambus.app.service.impl;

import flambus.app.entity.ReviewTagType;
import flambus.app.repository.ReviewRepository;
import flambus.app.repository.ReviewTagTypeRepository;
import flambus.app.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewTagTypeRepository reviewTagTypeRepository;

    /**
     * @title 리뷰 작성
     * @created 23.10.08
     * @Author 최성우
     */
    @Override
    public void createReview() {

    }

    /**
     * @title 해당 게시글에 작성된 리뷰 개수 확인.
     * @return 리뷰개수
     */
    @Override
    public long getTotalReviewCount(long storeIdx) {
        return reviewRepository.countByStoreIdx(storeIdx);
    }


    /**
     * @title StoreIdx의 대표 리뷰(탐험일지)
     * @param storeIdx
     * @return
     */
    @Override
    public long getRepresentReivew(long storeIdx) {
        return 0;
    }


    /**
     * @title 리뷰타입 정보 출력
     * @param idx
     * @return
     */
    @Override
    public ReviewTagType getReivewTypeByIdx(long idx) {
        Optional<ReviewTagType> reviewTag = reviewTagTypeRepository.findById(idx);
        System.out.println("reviewTag : "+reviewTag.get());
        return reviewTag.orElse(null);
    }
}
