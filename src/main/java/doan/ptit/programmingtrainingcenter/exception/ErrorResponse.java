package doan.ptit.programmingtrainingcenter.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private int statusCode;
    private String message;
    private Date timestamp;
    private String path;
    private String error;

}
