package com.example.exception;

import lombok.Getter;

@Getter
public enum MessageType {

	NO_RECORD_EXIST("1004", "kayıt bulunamadı"),
    TOKEN_IS_EXPIRED("1005", "Token süresi bitmiştir."),
    USERNAME_NOT_FOUND("1006","Kullanıcı Bulunamadı"),
    USERNAME_OR_PASSWORD_INVALID("1007","Kullanıcı Adı yada şifre hatalı"),
    REFRESH_TOKEN_NOT_FOUND("1008","Refresh Token Bulunamadı"),
    REFRESH_TOKEN_IS_EXPIRED("1009","Refresh Token süresi bitmiştir."),
    GENERAL_EXCEPTION("9999", "genel bir hata oluştu");


	private String code;

	private String message;

	MessageType(String code, String message) {
		this.code = code;
		this.message = message;

	}

}