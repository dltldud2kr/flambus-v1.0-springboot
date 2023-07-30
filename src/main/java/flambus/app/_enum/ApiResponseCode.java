package flambus.app._enum;

import org.springframework.http.HttpStatus;

public enum ApiResponseCode {
    SUCCESS(200, "Success"),
    CREATED(201, "Created Success"),
    ACCEPTED (202," 클라이언트의 요청은 정상적이나, 서버가 요청을 완료하지 못했습니다."),
    BAD_REQUEST(400, "Bad Request"), // 해당 코드 반환시  파라미터의 위치(path, query, body), 사용자 입력 값, 에러 이유를 꼭 명시하는 것이 좋다.
    UNAUTHORIZED(401, "권한 정보가 없는 토큰입니다."),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

