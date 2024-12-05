package doan.ptit.programmingtrainingcenter.exception;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RuntimeException.class , MethodArgumentNotValidException.class , BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception e , WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus("error");
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setPath(request.getDescription(false).replace("uri=" , ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return errorResponse;
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus("error");
        errorResponse.setMessage("Token JWT đã hết hạn");
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return errorResponse;
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleExpiredJwtException(TokenExpiredException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus("error");
        errorResponse.setMessage("Token JWT null");
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setTimestamp(new Date(System.currentTimeMillis()));
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return errorResponse;
    }






}
