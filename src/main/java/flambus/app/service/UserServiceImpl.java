package flambus.app.service;

import flambus.app.configuration.JwtProvider;
import flambus.app.dto.ResultDTO;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    @Autowired
    private JwtProvider jwtProvider;
    private Long expiredTimeMs = 1000 * 60 * 1L;   // 1분




    public String createToken(String userEmail)  {
//        String token = jwtProvider.createToken(userEmail); // 토큰 생성
        return jwtProvider.createToken(userEmail);
    }
}
