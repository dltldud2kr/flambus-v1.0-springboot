package flambus.app.controller;

import flambus.app._enum.ApiResponseCode;
import flambus.app._enum.AttachmentType;
import flambus.app.dto.ResultDTO;
import flambus.app.model.UploadImage;
import flambus.app.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UploadController {
    private final UploadService uploadService;

    /**
     *
     * @param image 이미지
     * @param userId 사용자 유저 아이디
     * @return
     * @throws IOException
     */
    @PutMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultDTO<Map<String,Object>> saveImage(
            @RequestParam(value="image") List<MultipartFile> image,
            @RequestParam String userId) throws IOException {

        Map<String,Object> sampleArray = new HashMap<>();
        uploadService.upload(image,userId ,AttachmentType.REVIEW,2344);
        return ResultDTO.of(ApiResponseCode.CREATED.getCode(),ApiResponseCode.CREATED.getMessage(), sampleArray);
    }

    @GetMapping("/image/{id}")
    public ResultDTO<UploadImage> getImageById(@PathVariable Long id) {
        try{
            Optional<UploadImage> image = uploadService.getImageById(id);
            if (!image.isPresent()) {
                return ResultDTO.of(ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage(), null);
            } else {
                return ResultDTO.of(ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage(), image.get());
            }
        } catch(NullPointerException error) {
            Optional<UploadImage> image = uploadService.getImageById(id);
            return ResultDTO.of(200,"success",image.get());
        }
    }

    @GetMapping("/image/{attachment}/{mappedId}")
    public ResultDTO<List<UploadImage>> getImageByAttachmentType(
            @PathVariable String attachment,
            @PathVariable long mappedId) {
        List<UploadImage> results = uploadService.getImageByAttachmentType(AttachmentType.fromString(attachment),mappedId);
        return ResultDTO.of(ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage(), results);
    }
}
