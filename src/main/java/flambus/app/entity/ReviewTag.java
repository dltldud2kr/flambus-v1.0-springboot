package flambus.app.entity;//package flambus.flambus_v10.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "review_tag")
public class ReviewTag {
    @Id  @GeneratedValue
    private Long idx;//pk

    //가게 idx값이 있는 이유는 해당 가게에서 제일 많이 달린 태그를 알아야하기때문임.
    private Long storeIdx; //리뷰가 달린 가게 idx
    private Long reviewIdx; //해당 리뷰 idx
    private Long tagIdx; //태그 idx
    private LocalDateTime created;
    private LocalDateTime modified;

}
