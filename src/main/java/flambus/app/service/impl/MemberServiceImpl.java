package flambus.app.service.impl;

import flambus.app._enum.CustomExceptionCode;
import flambus.app.auth.JwtTokenProvider;
import flambus.app.dto.email.emailResponseDto;
import flambus.app.dto.member.JoinRequestDto;
import flambus.app.dto.member.MemberDto;
import flambus.app.dto.member.TokenDto;
import flambus.app.entity.Member;
import flambus.app.exception.CustomException;
import flambus.app.mapper.MemberMapper;
import flambus.app.repository.MemberRepository;
import flambus.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberMapper memberMapper;

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
                    .isAdmin(false)
                    .platform(0)
                    .introduce(null)
                    .refreshToken(null)
                    .serviceAgree(false)
                    .serviceAgreeDate(LocalDateTime.now())
                    .termsAgree(false)
                    .profileImageUrl("test")                // 추가 (널값허용안받음)
                    .userName("test")                       // 추가 (널값허용안받음)
                    .withdrawal(false)                      // 추가 (널값허용안받음)
                    .withdrawalDate(LocalDateTime.now())    //널값허용왜안됨?
                    .subscriptionDate(LocalDateTime.now())  // 추가
                    .termsAgreeDate(LocalDateTime.now())
                    .emailAuth(false)                       // 추가
                    .useGpsAgree(false)
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

    //가입된 사용자 정보를 조회
    //탈퇴된 사용자는 제외하고 조회합니다.
    public List<MemberDto> getAllMembers() {
        //Optional<Member> member = memberRepository.findAll(memberIdx);
        //return member.orElse(null);
        MemberDto parameter = new MemberDto();
        List<MemberDto> list = memberMapper.selectList(parameter);
        return list;
    }

    @Override
    public boolean isAdmin(long memberIdx) {
        return memberRepository.findById(memberIdx).get().isAdmin();
    }

    /**
     * @title 도토리 지급을 위한 함수입니다.
     * @param member 지급할 사용자
     * @param count 지급할 도토리 개수
     * @return
     */
    @Override
    public long addAcorns(Member member, int count) {
        Long acornsCount = member.getAcornsCount() + count;
        member.setAcornsCount(acornsCount);
        memberRepository.save(member);
        return acornsCount;
    }

    /**
     * @title 사용자 보유 도토리를 감소시킵니다.
     * @param member 감소시킬 사용자
     * @param count 수소시킬 도토리 개수
     * @return
     */
    @Override
    public long removeAcorns(Member member, int count) {
        if(member.getAcornsCount() == 0) {
            return 0;
        }
        Long acornsCount = member.getAcornsCount() - count;
        member.setAcornsCount(acornsCount);
        memberRepository.save(member);
        return acornsCount;
    }


    @Override
    public ResponseEntity emailCheck(emailResponseDto dto) {

        System.out.println("test");
        System.out.println(dto.getEmail());
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Cannot find user with userId " + dto.getEmail()));

        System.out.println(member.getEmail());

        member.setEmailAuth(true);

        memberRepository.save(member);
        return ResponseEntity.ok().body("인증완료");
    }

}
