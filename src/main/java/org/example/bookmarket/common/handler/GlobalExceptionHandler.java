package org.example.bookmarket.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.common.handler.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.ExecutionException; // [추가]

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 우리가 직접 정의한 CustomException을 처리하는 핸들러
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponse.of(e.getMessage()));
    }

    /**
     * [추가] 비동기 작업에서 발생하는 ExecutionException을 처리하는 핸들러
     * 실제 원인(cause)을 추출하여 더 구체적인 에러를 로깅하고 응답합니다.
     */
    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<ErrorResponse> handleExecutionException(ExecutionException e) {
        Throwable cause = e.getCause() != null ? e.getCause() : e;
        log.error("ExecutionException occurred: {}", cause.getMessage(), cause);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        String message = "비동기 처리 중 오류가 발생했습니다: " + cause.getMessage();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(message));
    }

    /**
     * 예상치 못한 모든 예외를 처리하는 핸들러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        ErrorCode internalServerError = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(internalServerError.getStatus())
                .body(ErrorResponse.of(internalServerError.getMessage()));
    }
}