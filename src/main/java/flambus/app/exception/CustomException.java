package flambus.app.exception;


import flambus.app._enum.CustomExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException {
    private CustomExceptionCode customErrorCode;
    private String detailMessage;

    public CustomException(CustomExceptionCode customExceptionCode) {
        super(customExceptionCode.getStatusMessage());
        this.customErrorCode = customExceptionCode;
        this.detailMessage = customExceptionCode.getStatusMessage();
    }

}
