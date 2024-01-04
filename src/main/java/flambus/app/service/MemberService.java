package flambus.app.service;


import flambus.app.dto.email.emailResponseDto;
import flambus.app.dto.member.JoinRequestDto;
import flambus.app.dto.member.MemberDto;
import flambus.app.dto.member.TokenDto;
import flambus.app.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface MemberService {

    TokenDto login(String email, String password);
    TokenDto createToken(Long memberIdx);
    boolean join(JoinRequestDto request);

    Member getMember(String email);

    Member getMember(long memberIdx);

    boolean isMember(String email);

    Member isAlreadyEmail(String email);

    List<MemberDto> getAllMembers();

    boolean isAdmin(long memberIdx);

    @Transactional
    long addAcorns(Member member, int count);

    @Transactional
    long removeAcorns(Member member, int count);

    /**
     * 이메일 인증확인 (발송된 인증번호와 동일한지 확인)
     * @param email
     * @param verifCode
     * @return
     */
    boolean emailCheck(String email, String verifCode);




//    ResponseEntity pwEmailCheck(String email, String verifCode);


    /**
     * 비밀번호 변경
     */

//    Boolean changePw(String email, String password);


}
