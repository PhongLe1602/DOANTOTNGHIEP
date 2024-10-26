package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TokenResponse  {

    private String accessToken;

    private String refreshToken;

    private String userId;

    private String profilePicture;
}
