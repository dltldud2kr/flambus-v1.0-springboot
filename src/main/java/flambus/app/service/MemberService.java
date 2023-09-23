package flambus.app.service;

import flambus.app._enum.ApiResponseCode;
import flambus.app._enum.CustomExceptionCode;
import flambus.app.auth.JwtTokenProvider;
import flambus.app.dto.ResultDTO;
import flambus.app.dto.member.JoinRequestDto;
import flambus.app.dto.member.TokenDto;
import flambus.app.entity.Member;
import flambus.app.exception.CustomException;
import flambus.app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 1. 로그인 요청으로 들어온 ID, PWD 기반으로 Authentication 객체 생성
     * 2. authenticate() 메서드를 통해 요청된 Member에 대한 검증이 진행 => loadUserByUsername 메서드를 실행. 해당 메서드는 검증을 위한 유저 객체를 가져오는 부분으로써, 어떤 객체를 검증할 것인지에 대해 직접 구현
     * 3. 검증이 정상적으로 통과되었다면 인증된 Authentication객체를 기반으로 JWT 토큰을 생성
     */
    @Transactional
    public TokenDto login(String email, String password) {
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_EMAIL));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        return tokenDto;
    }

    public TokenDto createToken(Long memberIdx) {
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND));

        if (jwtTokenProvider.validateToken(member.getRefreshToken())) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
            System.out.println("authenticationToken : "+authenticationToken);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
            return tokenDto;
        } else {
            //만료된 리프레쉬 토큰.
            throw new CustomException(CustomExceptionCode.EXPIRED_JWT);
        }
    }


    //회원가입 로직
    @Transactional
    public boolean join(JoinRequestDto request) {
        try {
            //해당 이메일이 존재하는지 확인.
            if(this.getMember(request.getEmail()) != null) {
                throw new CustomException(CustomExceptionCode.DUPLICATED);
            }
            //해당 이메일이 디비에 존재하는지 확인.
            Member member = Member.builder()
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .isAdmin(0)
                    .platform(0)
                    .introduce(null)
                    .refreshToken(null)
                    .serviceAgree(0)
                    .serviceAgreeDate(LocalDateTime.now())
                    .termsAgree(0)
                    .termsAgreeDate(LocalDateTime.now())
                    .useGpsAgree(0)
                    .useGpsAgreeDate(LocalDateTime.now())
                    .follower(0)
                    .following(0)
                    .build();
            memberRepository.save(member);
            return true;
        } catch (DataAccessException e) {
            System.err.println("DataAccessException : " + e);
            return false;
        }
    }

    //이메일 -> 사용자 정보를 찾아고  pk
    public Member getMember(String email) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        // 비어있는 경우 예외 처리 또는 기본값을 반환하는 로직 추가

        return byEmail.orElse(null);
    }

    public Member getMember(long memberIdx) {
        Optional<Member> member = memberRepository.findById(memberIdx);
        // 비어있는 경우 예외 처리 또는 기본값을 반환하는 로직 추가
        return member.orElse(null);
    }



}
