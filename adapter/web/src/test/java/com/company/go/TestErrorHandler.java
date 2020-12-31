package com.company.go;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TestErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    TestError errorResolver(Exception anyException){
        String message = StringUtils.isEmpty(anyException.getMessage()) ? "Exception Occurred" : anyException.getMessage();
        return new TestError(message);
    }

    @Getter
    @AllArgsConstructor
    static public class TestError{
        private String errorMessages;
    }
}
