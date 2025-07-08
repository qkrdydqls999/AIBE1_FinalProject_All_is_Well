package org.example.bookmarket.common.handler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String message; // 이 필드는 ErrorCode의 메시지 또는 상세 메시지를 저장합니다.
    private final ErrorCode errorCode; // ErrorCode 객체 자체를 저장합니다.

    // 기존 생성자 1: HttpStatus와 메시지를 직접 받는 경우 (주로 GlobalExceptionHandler에서 사용)
    public CustomException(final HttpStatus statusCode, final String message) {
        super(message); // RuntimeException의 메시지로 전달
        this.statusCode = statusCode;
        this.message = message;
        this.errorCode = null; // 이 생성자에서는 ErrorCode가 없으므로 null
    }

    // 기존 생성자 2: ErrorCode만 받는 경우 (가장 일반적인 사용)
    public CustomException(final ErrorCode errorCode) {
        super(errorCode.getMessage()); // RuntimeException의 메시지로 ErrorCode의 메시지 전달
        this.errorCode = errorCode;
        this.statusCode = errorCode.getStatus();
        this.message = errorCode.getMessage(); // ErrorCode의 메시지를 저장
    }

    /**
     * [추가] ErrorCode와 상세 메시지를 함께 받을 수 있는 생성자.
     * 로그나 디버깅 시 더 구체적인 원인을 파악하는 데 도움이 됩니다.
     * @param errorCode 발생한 에러의 종류를 정의하는 ErrorCode
     * @param detailMessage 로깅을 위한 구체적인 에러 메시지
     */
    public CustomException(final ErrorCode errorCode, final String detailMessage) {
        super(detailMessage); // RuntimeException의 메시지로 상세 메시지 전달
        this.errorCode = errorCode;
        this.statusCode = errorCode.getStatus();
        this.message = detailMessage; // 상세 메시지를 저장
    }
}