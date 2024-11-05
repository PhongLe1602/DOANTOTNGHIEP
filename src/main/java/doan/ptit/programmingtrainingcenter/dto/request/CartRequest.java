package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class CartRequest {
    private String userId;
    private String courseId;
    private int quantity;
}
