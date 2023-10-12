package flambus.app.email;

public interface EmailService {
    String sendSimpleMessage(String to)throws Exception;
}