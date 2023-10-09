package flambus.app.service.impl;

import flambus.app._enum.AttachmentType;
import flambus.app._enum.CustomExceptionCode;
import flambus.app.dto.review.ReviewRequestDto;
import flambus.app.dto.store.StoreJounalDto;
import flambus.app.entity.Review;
import flambus.app.entity.ReviewTagType;
import flambus.app.entity.UploadImage;
import flambus.app.exception.CustomException;
import flambus.app.repository.ReviewRepository;
import flambus.app.repository.ReviewTagTypeRepository;
import flambus.app.service.ReviewService;
import flambus.app.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewTagTypeRepository reviewTagTypeRepository;
    @Autowired
    private UploadService uploadService;

    /**
     * @title 리뷰 작성
     * @created 23.10.08
     * @Author 최성우
     */
    @Override
    @Transactional
    public void createJournal(ReviewRequestDto request) throws IOException {
        //업로드한 리뷰 이미지가 존재한다면 리뷰 이미지 업로드를 진행합니다.

        try {
            Review review = new Review();

            review.setContent(request.getContent());
            review.setMemberIdx(request.getMemberIdx());
            review.setStoreIdx(request.getStoreIdx());
            review.setCreated(LocalDateTime.now());
            review.setModified(LocalDateTime.now());

            //리뷰를 먼저 생성하는 이유는 생성된 IDX로 업로드 이미지를 맵핑해주기 위해 1차적으로 먼저 생성
            Review savedReivew = reviewRepository.save(review);

            //정상적으로 리뷰가 생성됐는 경우 리뷰에 등록할 이미지를 첨부했는지 확인하고 해당 리뷰 IDX에 이미지 업로드를 실행함
            uploadService.upload(request.getReviewImage(), request.getMemberIdx(), AttachmentType.REVIEW, savedReivew.getIdx());
            //업로드에 성공했다면 해당 리뷰에 업로드된 이미지 중 0번째 이미지를 대표 이미지로 지정함.
            review.setRepresentIdx(uploadService.getImageByAttachmentType(AttachmentType.REVIEW, savedReivew.getIdx()).get(0).getIdx());

            //다시한번 저장.
            reviewRepository.save(review);
        } catch (CustomException e) {
            System.out.println("create Journal Error : " + e);
            new CustomException(CustomExceptionCode.SERVER_ERROR);
        }
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
    public long getRepresentReivewIdx(long storeIdx) {
        return 0;
    }

    /**
     * @title 가게 탐험일지 리스트 요청
     * @param storeIdx
     * @param pageNum 페이지 넘버
     * @param pageSize 각 페이지 표시 개수.
     * @return
     */
    @Override
    public List<StoreJounalDto> getStoreJounalList(long storeIdx, int pageNum,int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<Review> byStoreIdx = reviewRepository.findByStoreIdx(storeIdx,pageable);

        //해당 가게에 작성된 리뷰가 0개인 경우
        if(byStoreIdx.size() == 0) {
            throw new CustomException(CustomExceptionCode.NOT_FOUND);
        }

        List<StoreJounalDto> storeJounalDtos = new ArrayList<>();

        //작성된 리뷰를 DTO로 변환합니다.
        for (Review review : byStoreIdx) {

            List<UploadImage> imageByAttachmentType = uploadService.getImageByAttachmentType(AttachmentType.REVIEW, review.getIdx());

            //작성된 리뷰에 업로드된 이미지 정보를 가져옵니다.
            Map<String,Object> reviewImage = new HashMap<>();
            List<Map<String,Object>> imageList = new ArrayList<>();

            for (UploadImage uploadImage : imageByAttachmentType) {
                reviewImage.put("imageUrl",uploadImage.getImageUrl());
                reviewImage.put("fileName",uploadImage.getFileName());
                reviewImage.put("fileSize",uploadImage.getFileSize());
            }
            imageList.add(reviewImage);

            //완성된 정보를 Dto에 맵핑
            storeJounalDtos.add(StoreJounalDto.builder()
                    .idx(review.getIdx())
                    .content(review.getContent())
                    .memberIdx(review.getMemberIdx())
                    .jounalImage(imageList)
                    .created(review.getCreated())
                    .modified(review.getModified())
                    .build());
        }

        return storeJounalDtos;
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
