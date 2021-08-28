package com.stepanov.uocns.common.handling.advice;

import com.stepanov.uocns.common.handling.exceptions.AlreadyExistsException;
import com.stepanov.uocns.common.handling.exceptions.BadRequestException;
import com.stepanov.uocns.common.handling.exceptions.CommonException;
import com.stepanov.uocns.common.handling.exceptions.InternalErrorException;
import com.stepanov.uocns.common.handling.exceptions.NotAllowedException;
import com.stepanov.uocns.common.handling.exceptions.NotAuthorizedException;
import com.stepanov.uocns.common.handling.exceptions.ObjectNotFoundException;
import com.stepanov.uocns.common.handling.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Scanner;

@ControllerAdvice
public class CommonControllerAdvice {
    protected static final Logger log = LoggerFactory.getLogger(CommonControllerAdvice.class);

    @ExceptionHandler(ObjectNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleObjectNotFoundException(ObjectNotFoundException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalErrorException.class)
    public final ResponseEntity<ErrorResponse> handleInternalErrorException(InternalErrorException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAllowedException.class)
    public final ResponseEntity<ErrorResponse> handleNotAllowedException(NotAllowedException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public final ResponseEntity<ErrorResponse> handleNotAuthorizedException(NotAuthorizedException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.UNAUTHORIZED);
    }

    private String getRequestURL(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURL().toString();
    }

    private String getRequestBody(HttpServletRequest httpRequest) {
        String body = null;
        if (httpRequest.getMethod().equalsIgnoreCase("POST") || httpRequest.getMethod().equalsIgnoreCase("PUT")) {
            Scanner s;
            try {
                s = new Scanner(httpRequest.getInputStream(), "UTF-8").useDelimiter("\\A");
            } catch (IOException ex) {
                return ex.getMessage();
            }
            body = s.hasNext() ? s.next() : "";
        }
        return body;
    }

    @ResponseBody
    private ResponseEntity<ErrorResponse> handleMicroserviceException(CommonException ex, WebRequest request, HttpStatus httpStatus) {
        HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();

        String requestBody = getRequestBody(httpRequest);
        String requestURL = getRequestURL(httpRequest);

        String errorMessage = String.format("exception: %s. url: %s. body: %s",
                ex.getMessage(),
                requestURL,
                requestBody);

        log.error(errorMessage);

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(String.valueOf(httpStatus.value()))
                .error(ex.staticMessage())
                .build(), httpStatus);
    }
}
