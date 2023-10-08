package flambus.app.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @title 리뷰에 추가할수있는 태그 종류 엔티티 입니다.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "review_tag_type")
public class ReviewTagType {

    @Id
    @GeneratedValue
    private Long idx;//태그 고유 idx
    private String tagName; //태그 이름.

}
