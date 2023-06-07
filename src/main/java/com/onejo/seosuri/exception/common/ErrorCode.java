package com.onejo.seosuri.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /*성공 1000*/
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /*공통 2000*/
    INVALID_INPUT(false, 2000, "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(false, 2001, "잘못된 호출입니다."),
    HANDLE_ACCESS_DENIED(false, 2002, "접근할 수 없습니다."),
    INTERNAL_SERVER_ERROR(false, 2003, "문제가 발생했습니다."),
    FAILED_TO_SEND_EMAIL(false, 2004, "이메일 발송에 실패했습니다"),
    EMPTY_DATA(false, 2005, "비어 있는 값을 주셨습니다."),

    /*유저 3000*/
    ACCOUNT_NOT_FOUND(false, 3001, "사용자를 찾을 수 없습니다."),
    DUPLICATE_ACCOUNT(false, 3002, "이미 사용중인 이메일입니다."),
    EMAIL_SENDING_ERROR(false, 3003, "이메일 전송에 실패하였습니다."),
    POST_ACCOUNTS_EMPTY_EMAIL(false, 3004, "이메일을 입력해주세요."),
    POST_ACCOUNTS_EMPTY_NAME(false, 3005, "이름을 입력해주세요."),
    POST_ACCOUNTS_INVALID_EMAIL(false, 3006, "이메일 형식이 잘 못 되었습니다."),
    FAILED_TO_LOGIN(false, 3007, "비밀번호가 틀렸습니다."),

    /*서버, DB 4000*/
    DATABASE_ERROR(false, 4000, "DB에 문제가 발생했습니다."),
    NO_EXIST_CATEGORY_TITLE(false, 4001, "없는 카테고리 입니다."),
    NO_EXIST_PROBLEM(false, 4002, "문제가 존재하지 않습니다."),
    NO_EXIST_TEST_PAPER(false, 4003, "시험지가 존재하지 않습니다."),

    /*OCR, 5000*/
    NO_EXIST_FILE(false, 5001, "없는 파일 입니다."),
    FILE_IO_ERROR(false, 5002, "사진 파일을 읽는 도중 문제가 발생했습니다."),
    OCR_CLIENT_CREATION_ERROR(false, 5003, "OCR 클라이언트 생성에 실패했습니다."),
    OCR_PROCESSING_ERROR(false, 5004, "OCR 프로세싱 과정 중 문제가 발생했습니다."),
    IMAGE_TO_TEXT_PROCESSING_ERROR(false, 5005, "이미지를 텍스트로 변환하는 도중 문제가 발생했습니다.")

    ;


    private final boolean isSuccess;
    private final int code;
    private final String message;

    ErrorCode(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
