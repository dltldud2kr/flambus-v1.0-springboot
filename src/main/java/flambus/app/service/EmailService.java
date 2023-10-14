package flambus.app.service;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<String> sendEmailVerification(String to)throws Exception;
}
