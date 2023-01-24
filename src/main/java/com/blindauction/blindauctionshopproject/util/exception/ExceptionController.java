package com.blindauction.blindauctionshopproject.util.exception;

import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<StatusResponse> illegalArgumentExceptionhandler(IllegalArgumentException e){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponse, httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusResponse> methodValidException(MethodArgumentNotValidException e){
        StatusResponse statusResponseDto = new StatusResponse(HttpStatus.BAD_REQUEST.value(),e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponseDto,httpHeaders,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<StatusResponse> AccessDeniedExceptionHandler(AccessDeniedException e){
        StatusResponse statusResponseDto = new StatusResponse(HttpStatus.FORBIDDEN.value(),e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponseDto,httpHeaders,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    private ResponseEntity<StatusResponse> JwtExceptionHandler(JwtException e){
        StatusResponse statusResponseDto = new StatusResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponseDto,httpHeaders,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SecurityException.class)
    private ResponseEntity<StatusResponse> SecurityExceptionHandler(SecurityException e) {
        StatusResponse statusResponseDto = new StatusResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponseDto,httpHeaders,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    private ResponseEntity<StatusResponse> MalformedJwtExceptionHandler(MalformedJwtException e){
        StatusResponse statusResponseDto = new StatusResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponseDto,httpHeaders,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    private ResponseEntity<StatusResponse> SignatureExceptionHandler(SignatureException e){
        StatusResponse statusResponseDto = new StatusResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponseDto,httpHeaders,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<StatusResponse> ConstraintViolationExceptionHandler(ConstraintViolationException e){
        String message = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList()
                .get(0);
        StatusResponse statusResponseDto = new StatusResponse(HttpStatus.BAD_REQUEST.value(), message);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponseDto,httpHeaders,HttpStatus.BAD_REQUEST);
    }
@ExceptionHandler(IOException.class)
private ResponseEntity<StatusResponse> IOExceptionHandler(IOException e){
    StatusResponse statusResponse = new StatusResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(statusResponse, httpHeaders, HttpStatus.FORBIDDEN);
}

    @ExceptionHandler(ServletException.class)
    private ResponseEntity<StatusResponse> ServletExceptionHandler(ServletException e){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(statusResponse, httpHeaders, HttpStatus.FORBIDDEN);
    }

}
