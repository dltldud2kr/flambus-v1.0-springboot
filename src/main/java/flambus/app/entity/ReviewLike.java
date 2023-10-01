package flambus.app.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class ReviewLike {
    @Id
    @GeneratedValue
    private Long idx;//pk
    private Long tagCategory;  //서비스,분위기,맛
    private String tagName; //태그 이름.
}
