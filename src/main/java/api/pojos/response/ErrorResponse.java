package api.pojos.response;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ErrorResponse {

    private final String errorMessage;
    private final int errorCode;
}
