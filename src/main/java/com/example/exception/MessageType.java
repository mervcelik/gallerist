package com.example.exception;

import lombok.Getter;

@Getter
public enum MessageType {

	NO_RECORD_EXIST("1004", "kayıt bulunamadı"),
    TOKEN_IS_EXPIRED("1005", "Token süresi bitmiştir."),
    USERNAME_NOT_FOUND("1006","Kullanıcı Bulunamadı"),
    GENERAL_EXCEPTION("9999", "genel bir hata oluştu");


	private String code;

	private String message;

	MessageType(String code, String message) {
		this.code = code;
		this.message = message;

	}

}