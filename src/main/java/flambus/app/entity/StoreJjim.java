package flambus.app.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * store 찜하기.(나만의 탐험지로 지정)
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "store_jjim")
public class StoreJjim {
    @Id
    @GeneratedValue
    private Long idx;//좋아요 idx
    private Long storeIdx; //스토어 IDX
    private Long memberIdx; //멤버
    private LocalDateTime created; //찜 한 날짜

}
