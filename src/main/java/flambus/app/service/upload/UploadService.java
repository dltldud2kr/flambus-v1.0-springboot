package flambus.app.service.upload;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import flambus.app._enum.FileType;
import flambus.app.dto.ResultDTO;
import flambus.app.model.UploadImage;
import flambus.app.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

//import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class UploadService {

    private final AmazonS3Client amazonS3Client;
    private final UploadRepository uploadRepository;

    //최대 업로드 가능한 단건 파일의 용량
    private static long MAX_SIZE = 10000;
    private static short MAX_UPLOAD_COUNT = 10;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * @param multipartFile 업로드된 파일 multipartFile 객체
     * @param userId        사용자 userId
     * @param uploadType    업로드 타입("REVIEW,FEED")
     * @return
     * @throws IOException
     * @title 파일 업로드
     */
    @Transactional
    public void upload(List<MultipartFile> multipartFile, String userId, String uploadType, long mappedId) throws IOException {
        //TODO 업로드 타입이 ENUM에 있는 값이 아니라면 예외처리 필요
        //TODO 업로드 이미지 용량 제한 필요.

        List<UploadImage> saveImageDataList = new ArrayList<>();

        if(multipartFile.size() <= 0) {
            new IllegalArgumentException("업로드될 파일이 없습니다.");
        }

        //MultipartFile로 받은 객체를 File 객체로 변환
        for (MultipartFile file : multipartFile) {
            //업로드 시도한 파일 제한 용량 검증
            validateFileSize(file);
            //MultipartFile -> File 객체로 convert
            File convertFile = convert(file).orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
            //s3에 적재될 유니크한 파일 이름
            String saveFileName = generateUniqueFileName(file.getName());
            //실제 파일 이름.
            String orginFileName = file.getOriginalFilename();
            //업로드될 버킷 PATH
            String bucketPath = uploadType + "/" + userId + "/" + mappedId + "/" + saveFileName;//적재할 경로 세팅

            //업로드 된 이미지 URL
            String url = putS3(convertFile, bucketPath); //s3에 적재
            // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
            removeNewFile(convertFile);

            //S3 버킷에 적재된 이미지 파일 정보를 게시글정보와 함께 맵핑해서 디비에 저장함.
            saveImageDataList.add(UploadImage.builder()
                    .fileName(orginFileName)
                    .uniqueFileName(saveFileName)
                    .imageUrl(url)
                    .fileSize(file.getSize())
                    .mappedType(uploadType)
                    .mappedId(mappedId)
                    .created(LocalDateTime.now())
                    .updated(LocalDateTime.now())
                    .build());
        }
        saveDB(saveImageDataList,uploadType,mappedId);
    }

    /**
     * @title 해당 피드,리뷰와 맵핑된 이미지 정보를 반환합니다.
     */
    private UploadImage getUploadImage(String mappedType, long mappedId) {
        Optional<UploadImage> byIdAndMappedType = uploadRepository.findByIdAndMappedType(mappedId, mappedType);
        return byIdAndMappedType.get();
    }

    //정상적으로 업로드된 파일정보를 데이터베이스에 저장하고 리뷰,피드 정보와 맵핑
    private List<Map<String, Object>> saveDB(List<UploadImage> saveImageData, String mappedType, long mappedId) {
        List<UploadImage> existingImages = uploadRepository.findByMappedTypeAndMappedId(mappedType, mappedId);

        // 이미 해당 리뷰 또는 피드에 업로드된 이미지가 있는 경우, 모두 삭제
        if (!existingImages.isEmpty()) {
            uploadRepository.deleteAll(existingImages);
        }

        // 새로운 이미지들을 저장
        List<UploadImage> savedImages = uploadRepository.saveAll(saveImageData);

        // 저장된 이미지들의 정보를 결과 리스트에 추가
        List<Map<String, Object>> results = new ArrayList<>();

        for (UploadImage savedImage : savedImages) {
            Map<String, Object> image = new HashMap<>();
            image.put("fileName", savedImage.getFileName());
            image.put("imageUrl", savedImage.getImageUrl());
            results.add(image);
        }

        return results;
    }

    private String putS3(File uploadFile, String fileName) {
        //todo 동일한 경로의 동일한 파일이 있다면?
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)    // PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * S3적재전 로컬에 저장되어있는 파일을 삭제합니다.
     * @param targetFile
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
    /**
     * @param originalFileName
     * @return uuid
     * @title 파일이름이 겹치지 않기 위한 유니크한 파일 이름을 만들어주는 함수.
     */
    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex >= 0) {
            extension = originalFileName.substring(lastDotIndex);
        }

        // UUID를 사용하여 랜덤값을 생성하고, 확장자와 합쳐서 고유한 파일 이름을 생성
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID + extension;
    }

    /**
     * @title multipart 파일 객체를 일반 File 객체로 변환
     * @param file
     * @author 최성우
     * @return
     * @throws IOException
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        try {
            File convertFile = new File(file.getOriginalFilename());

            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("e : " + e);
        }
        return Optional.empty();
    }

    /**
     * @title 파일의 최대 용량을 초과한 경우 예외 처리
     * @param file
     */
    private void validateFileSize(MultipartFile file) {
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        FileType fileType = FileType.fromContentType(contentType);

        if (fileType != null) {
            switch (fileType) {
                case ZIP:
                case PNG:
                case JPEG:
                case PDF:
                    if (fileSize > fileType.getMaxSize()) {
                        System.out.println(fileType.getContentType() + " : 업로드 제한된 용량 이상입니다.");
                        // ZIP 파일의 최대 용량을 초과한 경우 예외 처리
                        // throw new YourException("ZIP 파일 용량 초과"); // 예외 처리 방식은 상황에 맞게 정의
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
