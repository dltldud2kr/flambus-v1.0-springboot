package flambus.app.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //게시글 관련 조회
//                .antMatchers(HttpMethod.GET, "/api/v1/post/**").permitAll()
//                //서브 카테고리 조회
//                .antMatchers(HttpMethod.GET, "/api/v1/category/sub/**").permitAll()
//                //메인 카테고리 조회
//                .antMatchers(HttpMethod.GET, "/api/v1/category/main/**").permitAll()
//                //로그인 토큰 생성
//                .antMatchers(HttpMethod.GET, "/api/v1/users/login/kakao/**").permitAll()
//                //사용자 북마크 조회
//                .antMatchers(HttpMethod.GET, "/api/v1/category/sub/bookmark").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/v1/image").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/v1/**").permitAll()
//                .antMatchers(HttpMethod.DELETE, "/api/v1/**").permitAll()
//                .antMatchers(HttpMethod.PATCH, "/api/v1/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS, "/api/v1/**").permitAll()
//                .antMatchers(HttpMethod.GET,"/api/v1/**").permitAll()

                //위 허용사항 외에 모든 API 는 인증을 거친다.
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
