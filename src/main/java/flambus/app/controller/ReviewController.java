package flambus.app.controller;

import flambus.app._enum.ApiResponseCode;
import flambus.app.dto.ResultDTO;
import flambus.app.dto.review.ReviewRequestDto;
import flambus.app.exception.CustomException;
import flambus.app.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/journal")
@Tag(name = "가게 일지 관련 정보", description = "")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "가게의 탐험일지 요청", description = "현재 가게의 등록된 탐험일지 다건 리스트를 페이징으로 처리" +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
    })
    @GetMapping
    public ResultDTO getStoreExpJournal() {
        return null;
    }


    @Operation(summary = "신규 탐험일지 작성", description = "" +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "서버 요청 성공"),
    })
    @PutMapping
    public ResultDTO createJournal(ReviewRequestDto reviewRequestDto) {
        try {
            reviewService.createJournal(reviewRequestDto);
            return ResultDTO.of(true, ApiResponseCode.SUCCESS.getCode(), "리뷰가 정상적으로 등록되었습니다.", null);
        } catch (CustomException e) {
            return ResultDTO.of(false, e.getCustomErrorCode().getStatusCode(), e.getDetailMessage(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Operation(summary = "작성한 탐험일지 수정", description = "" +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
    })
    @PatchMapping
    public ResultDTO modifyJournal() {
        return null;
    }

    @Operation(summary = "작성한 탐험일지 삭제", description = "" +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
    })
    @DeleteMapping
    public ResultDTO removeJournal() {
        return null;
    }


    @Operation(summary = "다른 사용자 탐험일지 좋아요", description = "이미 좋아요 되어있는 경우 좋아요 취소" +
            "\n### HTTP STATUS 에 따른 조회 결과" +
            "\n- 200: 서버요청 정상 성공 "+
            "\n- 500: 서버에서 요청 처리중 문제가 발생" +
            "\n### Result Code 에 따른 요청 결과" +
            "\n- ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 요청 성공"),
    })
    @PostMapping("/like")
    public ResultDTO setJournalPostLike() {
        return null;
    }

}
