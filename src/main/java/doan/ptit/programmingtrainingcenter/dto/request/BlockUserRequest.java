package doan.ptit.programmingtrainingcenter.dto.request;

import lombok.Data;

@Data
public class BlockUserRequest {
    private String userId;
    private boolean blocked;
}
