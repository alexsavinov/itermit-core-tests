package api.pojos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String access_token;
    private String refresh_token;
    private Long id;
    private String username;
    private List<String> roles;
}