package flambus.app.dto.store;


import flambus.app.dto.member.MemberDto;
import lombok.*;

/**
 * @title 해당 가게의 탐험 일지 정보
 */

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreJounalDto {
    private Long storeIdx;
    private String storeName;
    private String storeAddress;
    private String contactNumber;
    private long expJournalsCount; //가게 탐험일지 개수
    private long ownExpSiteCount; //사용자들이 지정한 나만의 탐험지 개수(찜)
    private String storeTag; //가게의 태그(신선했던..맛있는 등등..)
    private String representImage; //대표 이미지
    private MemberDto representCreator; //대표 이미지 작성자 정보

}
