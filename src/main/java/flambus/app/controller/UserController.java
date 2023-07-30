package flambus.app.controller;

import flambus.app._enum.ApiResponseCode;
import flambus.app.dto.ResultDTO;
import flambus.app.service.UploadService;
import flambus.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {
    @Autowired
    private final UserService userService;


    private Long expiredTimeMs = 1000 * 60 * 10L;   // 10분으로 만들어도




    @PostMapping("/token")
    public ResultDTO<Object> setToken() {
        return ResultDTO.of(ApiResponseCode.CREATED.getCode(),ApiResponseCode.CREATED.getMessage(),userService.createToken("sqdf@naver.com") );
    }
}
