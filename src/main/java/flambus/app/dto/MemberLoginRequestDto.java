package flambus.app.dto;


import lombok.Data;

@Data
public class MemberLoginRequestDto {
    private String memberId;
    private String password;
}
