package flambus.app.service.impl;

import flambus.app._enum.CustomExceptionCode;
import flambus.app._enum.EmailAuthStatus;
import flambus.app.auth.JwtTokenProvider;
import flambus.app.dto.member.JoinRequestDto;
import flambus.app.dto.member.MemberDto;
import flambus.app.dto.member.TokenDto;
import flambus.app.entity.EmailAuth;
import flambus.app.entity.Member;
import flambus.app.exception.CustomException;
import flambus.app.mapper.MemberMapper;
import flambus.app.repository.EmailAuthRepository;
import flambus.app.repository.MemberRepository;
import flambus.app.service.MapService;
import flambus.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {


}
