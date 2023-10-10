package flambus.app.mapper;

import flambus.app.dto.review.ReviewResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface StoreMapper {

    Long findMostUsedTagIdx(@Param("storeIdx") long storeIdx);

}
