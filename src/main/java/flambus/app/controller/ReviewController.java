package flambus.app.controller;

import flambus.app._enum.ApiResponseCode;
import flambus.app.dto.ResultDTO;
import flambus.app.dto.review.CreateReviewRequestDto;
import flambus.app.dto.review.ModifyReviewRequestDto;
import flambus.app.dto.review.ReviewResponseDTO;
import flambus.app.entity.ReviewTagType;
import flambus.app.exception.CustomException;
import flambus.app.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/journal")
@Tag(name = "가게 일지 관련 정보", description = "")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "가게의 탐험일지 리스트 요청", description = "현재 가게의 등록된 탐험일지 다건 리스트를 페이징으로 처리" +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
    })

    @GetMapping
    public ResultDTO<List<ReviewResponseDTO.Review>> getStoreExpJournal(@RequestParam(value = "storeIdx") long storeIdx,
                                                                                @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            return ResultDTO.of(true, ApiResponseCode.SUCCESS.getCode(), "SUCCESS", reviewService.getStoreJounalList(storeIdx, pageNum, pageSize));
        } catch (CustomException e) {
            if (e.getCustomErrorCode().getStatusCode().equals("NOT_FOUND")) {
                return ResultDTO.of(false, e.getCustomErrorCode().getStatusCode(), "작성된 탐험일지가 없어요.", null);
            } else {
                return ResultDTO.of(false, ApiResponseCode.INTERNAL_SERVER_ERROR.getCode(), "서버 요청중 문제가 발생했어요.", null);
            }
        }
    }


    @Operation(summary = "신규 탐험일지 작성", description = "리뷰 작성시 보유 도토리 가 1개 추가로 증가합니다." +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "서버 요청 성공"),
    })
    @PutMapping
    public ResultDTO createJournal(CreateReviewRequestDto createReviewRequestDto) {
        try {
            reviewService.createJournal(createReviewRequestDto);
            return ResultDTO.of(true, ApiResponseCode.SUCCESS.getCode(), "리뷰가 정상적으로 등록되었습니다.", null);
        } catch (CustomException e) {
            return ResultDTO.of(false, e.getCustomErrorCode().getStatusCode(), e.getDetailMessage(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Operation(summary = "작성한 탐험일지 수정", description = "리뷰 작성과 동일한 로직입니다. 기존 작성된 리뷰가 삭제되고 해당 리뷰 IDX로 그대로 덮어집니다." +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
    })
    @PatchMapping
    public ResultDTO modifyJournal(ModifyReviewRequestDto modifyReviewRequestDto) {
        try {
            reviewService.updateJournal(modifyReviewRequestDto);
            return ResultDTO.of(true, ApiResponseCode.SUCCESS.getCode(), "리뷰가 정상적으로 수정되었어요.", null);
        } catch (CustomException e) {
            if(e.getCustomErrorCode().getStatusCode().equals("ACCESS_DENIED")) {
                return ResultDTO.of(false, e.getCustomErrorCode().getStatusCode(),"리뷰 작성자만 수정할 수 있어요.", null);
            } else {
                return ResultDTO.of(false, ApiResponseCode.INTERNAL_SERVER_ERROR.getCode(), ApiResponseCode.INTERNAL_SERVER_ERROR.getMessage(), null);
            }
        }
    }

//    @Operation(summary = "작성한 탐험일지 삭제", description = "" +
//            "\n### HTTP STATUS 에 따른 조회 결과" +
//            "\n- 200: 서버요청 정상 성공 "+
//            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
//            "\n### Result Code 에 따른 요청 결과" +
//            "\n- ")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
//    })
//    @DeleteMapping
//    public ResultDTO removeJournal() {
//        return null;
//    }


//    @Operation(summary = "다른 사용자 탐험일지 좋아요", description = "이미 좋아요 되어있는 경우 좋아요 취소" +
//            "\n### HTTP STATUS 에 따른 조회 결과" +
//            "\n- 200: 서버요청 정상 성공 "+
//            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
//            "\n### Result Code 에 따른 요청 결과" +
//            "\n- ")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
//    })
//    @PostMapping("/like")
//    public ResultDTO setJournalPostLike() {
//        return null;
//    }


    @Operation(summary = "탐험일지 전체 태그 리스트", description = "현재 리뷰에 등록할 수 있는 전체 태그 리스트를 반환합니다.\n해당 API로 리뷰 작성시 사용자가 선택할 태그 구현하시면 됩니다." +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 " +
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
    })
    @GetMapping("/tag")
    public ResultDTO<List<ReviewTagType>> getReviewTag() {
        try {
            return ResultDTO.of(true, ApiResponseCode.SUCCESS.getCode(), "성공", reviewService.getReviewAllTag());
        } catch (CustomException e) {
            return ResultDTO.of(false, ApiResponseCode.INTERNAL_SERVER_ERROR.getCode(), ApiResponseCode.INTERNAL_SERVER_ERROR.getMessage(), null);
        }
    }

}
