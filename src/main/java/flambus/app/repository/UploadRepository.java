package flambus.app.repository;//package flambus.flambus_v10.repository;

import flambus.app.model.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadRepository  extends JpaRepository<UploadImage, String> {

    List<UploadImage> findByMappedTypeAndMappedId(String mappedType, long mappedId);

    void deleteAllByMappedTypeAndMappedId(String mappedType, long mappedId);

    Optional<UploadImage> findByIdAndMappedType(long id,String mappedType);


}
