package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class OrderCheckOutNowRequest {
    private String courseId;
    private String paymentMethodId;
}
