package flambus.app.repository;//package flambus.flambus_v10.repository;

import flambus.app._enum.AttachmentType;
import flambus.app.model.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadRepository  extends JpaRepository<UploadImage, String> {

    List<UploadImage> findByAttachmentTypeAndMappedId(String attachmentType, long mappedId);

    Optional<UploadImage> findByIdAndAttachmentType(String attachmentType, long id);


}
