package flambus.app.service.impl;

import flambus.app._enum.CustomExceptionCode;
import flambus.app._enum.EmailAuthStatus;
import flambus.app.entity.EmailAuth;
import flambus.app.entity.Member;
import flambus.app.exception.CustomException;
import flambus.app.repository.EmailAuthRepository;
import flambus.app.repository.MemberRepository;
import flambus.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EmailAuthRepository emailAuthRepository;

    public static final String ePw = createKey();

    private MimeMessage createMessage(String to) throws Exception {
        // 이메일 내용에 버튼을 추가한 HTML
        String emailHtml = "<html><body><h1>Welcome to My App</h1>"
                + "<p>Click the button below:</p>"
                + "<a href='http://localhost:2000/api/v1/emailConfirm/Auth?email="
                + to + "'" +
                " style='background-color: #008CBA; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;'>Click Me</a>"
                + "</body></html>";

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);
        message.setSubject("이메일 인증 테스트");
        message.setText(emailHtml, "utf-8", "html");
        message.setFrom(new InternetAddress("dltldud2kr@gmail.com", "leesiyoung"));

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public ResponseEntity<String> sendEmailVerification(String email) throws Exception {

        // 이미 존재하는 회원일 시 인증 메일을 보내지 않음. ( member 테이블에서 )
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
        }

        // 이미 가입된 회원인지 확인 ( emailAuth 테이블에서 )
        EmailAuth existingAuth = emailAuthRepository.findByEmailAndEmailAuthStatus(email, EmailAuthStatus.COMPLETED)
                .orElse(null);

        if (existingAuth != null) {
            return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
        }

        // 인증 완료 상태인 경우 예외 처리
        existingAuth = emailAuthRepository.findByEmailAndEmailAuthStatus(email, EmailAuthStatus.VERIFIED)
                .orElse(null);

        if (existingAuth != null) {
            return ResponseEntity.badRequest().body("이메일 인증이 완료된 회원입니다.");
        }

        // 전에 쌓인 인증되지 않은 EmailAuth 값들을 전부 "INVALID"로 변경
        List<EmailAuth> emailAuthList = emailAuthRepository.findListByEmailAndEmailAuthStatus(email, EmailAuthStatus.UNVERIFIED);
        for (EmailAuth emailAuth : emailAuthList) {
            emailAuth.setEmailAuthStatus(EmailAuthStatus.INVALID);
        }
        emailAuthRepository.saveAll(emailAuthList);

        // 이메일 보내는 로직
        MimeMessage message = createMessage(email);
        try {
            emailSender.send(message);
            //메일 인증 테이블
            EmailAuth newAuth = EmailAuth.builder()
                    .email(email)
                    .emailAuthStatus(EmailAuthStatus.UNVERIFIED)
                    .created(LocalDateTime.now())
                    .build();
            emailAuthRepository.save(newAuth);
            return ResponseEntity.ok("이메일을 성공적으로 보냈습니다.");
        } catch (MailException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }
}