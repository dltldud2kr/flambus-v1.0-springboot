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

    List<MemberDto> getAllMembers();

    boolean isAdmin(long memberIdx);

    @Transactional
    long addAcorns(Member member, int count);

    @Transactional
    long removeAcorns(Member member, int count);

    ResponseEntity emailCheck(emailResponseDto dto);
}
