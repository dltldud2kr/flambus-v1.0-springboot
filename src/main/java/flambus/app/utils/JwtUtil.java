package flambus.app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JwtUtil {
    public static String getUserId(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userId", String.class);
    }

    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

    public static String createJwt(String userId, String secretKey, Long expireTimeMs) {
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)  // 유저 id만 넣어놨음.
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 만든 날짜
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 끝나는 날짜
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }



}
