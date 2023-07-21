package flambus.app.controller;

import flambus.app._enum.AttachmentType;
import flambus.app.dto.ResultDTO;
import flambus.app.service.upload.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UploadController {
    private final UploadService uploadService;

    @PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultDTO<Map<String,Object>> saveImage(
            @RequestParam(value="image") List<MultipartFile> image,
            @RequestParam String userId
    ) throws IOException {
        Map<String,Object> sampleArray = new HashMap<>();
        System.out.println("AttachmentType.REVIEW : " + AttachmentType.REVIEW);

        uploadService.upload(image,userId ,AttachmentType.REVIEW,2344);
        return ResultDTO.of(200,"success",sampleArray);
    }
}
