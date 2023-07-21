package flambus.app.controller;

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
            @RequestParam String userId,
            @RequestParam String uploadType
    ) throws IOException {
        Map<String,Object> sampleArray = new HashMap<>();
        uploadService.upload(image,userId ,uploadType,2344);
        return ResultDTO.of(200,"success",sampleArray);
    }
}
