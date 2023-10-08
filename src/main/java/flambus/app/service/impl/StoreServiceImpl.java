package flambus.app.service.impl;

import flambus.app._enum.CustomExceptionCode;
import flambus.app.dto.member.MemberDto;
import flambus.app.dto.store.StoreDto;
import flambus.app.dto.store.StoreJounalDto;
import flambus.app.dto.store.StoreMapMarkerDto;
import flambus.app.entity.Review;
import flambus.app.entity.ReviewTag;
import flambus.app.entity.ReviewTagType;
import flambus.app.entity.Store;
import flambus.app.exception.CustomException;
import flambus.app.mapper.MemberMapper;
import flambus.app.mapper.StoreMapper;
import flambus.app.repository.ReviewTagTypeRepository;
import flambus.app.repository.StoreRepository;
import flambus.app.service.MemberService;
import flambus.app.service.ReviewService;
import flambus.app.service.StoreService;
import flambus.app.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    public List<StoreMapMarkerDto> getMapStoreMakrerInfo() {
        return null;
    }


    /**
     * @return
     * @title 가게 정보를 요청합니다.
     */
    @Override
    public StoreDto getStoreInfo(long storeIdx) {
        StoreDto storeDto = new StoreDto();
        //요청한 가게 정보 find
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND));

        //기본 가게 정보 세팅
        storeDto.setStoreIdx(store.getIdx());
        storeDto.setStoreAddress(store.getAddress());
        storeDto.setStoreName(store.getName());
        storeDto.setContactNumber(store.getContactNumber());

        //해당 가게의 작성된 일지 개수 확인
        long totalReviewCount = reviewService.getTotalReviewCount(storeIdx);
        storeDto.setExpJournalsCount(totalReviewCount);

        //가게에서 작성된 일지중 제일 많은 태그를 찾아옴.
        Map<String, Object> representTag = new HashMap<>();

        //작성된 리뷰 개수가 0 이상일 경우에만 대표 태그 검색
        if(totalReviewCount > 0) {
            ReviewTagType reviewType =  reviewService.getReivewTypeByIdx(storeMapper.findMostUsedTagIdx(storeIdx));
            // 리뷰 태그 타입을 사용하는 코드 작성
            representTag.put("tagIdx",reviewType.getIdx());
            representTag.put("tagName",reviewType.getTagName());
            storeDto.setRepresentTag(representTag);
        } else {
            //리뷰가 없는 경우 그냥 null 반환해줌.
            storeDto.setRepresentTag(null);
        }

        //해당 가게 대표 탐험일지
        Map<String, Object> representJournal = new HashMap<>();
        representJournal.put("journalIdx",1);
        representJournal.put("thumbnail","http://www.lampcook.com/wi_files/food_top100/top5/5_1.jpg");
        representJournal.put("memberIdx",1);
        representJournal.put("memberName","최성우");
        storeDto.setRepresentJournal(representJournal);


        //대표 일지 이미지 정보 및 작성자 정보 찾기
        System.out.println("storeDto : " + storeDto);

        // 비어있는 경우 예외 처리 또는 기본값을 반환하는 로직 추가
        return storeDto;
    }


    /**
     * @title 해당 가게의 사용자들이 작성한 탐험일지 정보 리스트 보기
     * @return
     */
    @Override
    public List<StoreJounalDto> getStoreExpJournal() {

        return null;
    }

    @Override
    public Store getStore(long storeIdx) {
        return null;
    }

}
