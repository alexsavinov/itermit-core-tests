package api.pojos.response;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {

    private String access_token;
    private String refresh_token;
}
