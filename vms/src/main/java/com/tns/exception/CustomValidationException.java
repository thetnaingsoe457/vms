package com.tns.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CustomValidationException(String obj, String message) {
		super(String.format("Validation failed [%s]: %s", obj, message));
	}
}
