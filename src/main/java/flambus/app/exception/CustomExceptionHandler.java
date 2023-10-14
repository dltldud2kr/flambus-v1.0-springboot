package flambus.app.exception;

import flambus.app.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getCustomErrorCode(), ex.getDetailMessage());
        return ResponseEntity.status(ex.getCustomErrorCode().getHttpStatus()).body(errorResponse);
    }
}
