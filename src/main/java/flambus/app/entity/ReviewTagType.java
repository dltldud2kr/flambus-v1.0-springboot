package flambus.app.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @title 리뷰에 추가할수있는 태그 종류 엔티티 입니다.
 */
public class ReviewTagType {

    @Id
    @GeneratedValue
    private Long idx;//태그 고유 idx
    private Long tagCategory;  //서비스,분위기,맛
    private String tagName; //태그 이름.

}
