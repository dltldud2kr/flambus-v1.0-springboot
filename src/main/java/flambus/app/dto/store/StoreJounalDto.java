package flambus.app.dto.store;


import flambus.app.dto.member.MemberDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @title 해당 가게의 탐험 일지 정보
 */

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreJounalDto {
    private Long idx;//리뷰 idx
    private Long memberIdx;//리뷰 작성자 idx
    private String content; //리뷰내용
    private List<Map<String,Object>> jounalImage;
    private LocalDateTime created; //작성 시간
    private LocalDateTime modified; //수정 시간

}
