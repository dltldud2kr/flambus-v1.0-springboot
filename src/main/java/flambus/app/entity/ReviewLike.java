package flambus.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "review_like")
public class ReviewLike {
    @Id
    @GeneratedValue
    private Long idx;//pk
    @NotNull
    private Long review_idx;  //서비스,분위기,맛
    @NotNull
    private Long member_idx;
    @NotNull
    private LocalDateTime created;
}
