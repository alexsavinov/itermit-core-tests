package api.pojos.request;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}
