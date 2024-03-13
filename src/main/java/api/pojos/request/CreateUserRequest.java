package api.pojos.request;

import lombok.*;
import api.pojos.Profile;

import java.util.Set;

@Data
@Builder
public class CreateUserRequest {

    private String username;
    private String password;
    private Set<String> role;
    private Profile profile;
}
