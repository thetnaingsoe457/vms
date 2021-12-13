package com.tns.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InternalServerException(String token, String message) {
		super(String.format("Failed for [%s]: %s", token, message));
	}
}
