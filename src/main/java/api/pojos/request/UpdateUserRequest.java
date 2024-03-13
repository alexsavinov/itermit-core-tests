package api.pojos.request;

import lombok.*;
import api.pojos.Profile;

import java.util.Set;

@Data
@Builder
public class UpdateUserRequest {

    private Long id;
    private String username;
    private Set<String> role;
    private Profile profile;
}