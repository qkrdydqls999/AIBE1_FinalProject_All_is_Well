package org.example.bookmarket.common.handler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // == User & Auth Errors == //
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),

    // == Common Errors == //
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),

    // === Profile Errors ===
    PROFILE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필의 사용자 정보를 찾을 수 없습니다."),
    INVALID_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "잘못된 프로필 이미지입니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),

    // === Category Errors ===
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CATEGORY_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),

    // === UsedBook Errors ===
    USED_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 중고도서를 찾을 수 없습니다."),
    USED_BOOK_IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "중고도서 등록 시 이미지는 최소 1장 이상 필요합니다."),
    INVALID_BOOK_CONDITION(HttpStatus.BAD_REQUEST, "잘못된 도서 상태 정보입니다."),

    // [추가] === Chat Errors ===
    CHAT_CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅 채널을 찾을 수 없습니다."),
    UNAUTHORIZED_CHAT_ACCESS(HttpStatus.FORBIDDEN, "해당 채팅방에 접근할 권한이 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메시지를 찾을 수 없습니다."),
    UNAUTHORIZED_MESSAGE_DELETE(HttpStatus.FORBIDDEN, "메시지를 삭제할 권한이 없습니다."),

    // == Book Errors == //
    INVALID_ISBN(HttpStatus.BAD_REQUEST, "올바른 ISBN 형식이 아닙니다."),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 도서를 찾을 수 없습니다."),

    // == 외부 시스템 연동 오류 ==
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 업로드에 실패했습니다."),
    AI_ANALYSIS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 이미지 분석에 실패했습니다."),

    // === External API Errors ===
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부도서 api 호출에 실패했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // Common Errors
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    PURCHASE_LOCK_FAILED(HttpStatus.CONFLICT, "현재 다른 사용자가 구매 중입니다. 잠시 후 다시 시도해주세요."),
    BOOK_ALREADY_SOLD(HttpStatus.CONFLICT, "이미 판매 완료된 상품입니다."),
    WISHLIST_DUPLICATED(HttpStatus.CONFLICT, "이미 찜한 책입니다."),
    WISHLIST_OWN_BOOK(HttpStatus.CONFLICT, "자신의 판매글은 찜할 수 없습니다."),

    USED_BOOK_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "판매글을 삭제할 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;
}