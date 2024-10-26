package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AuthResponse {
    private String status;
    private String message;
    private Date timestamp;
}
