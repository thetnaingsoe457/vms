package com.tns.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CreditCardException extends RuntimeException {
	private static final long serialVersionUID = -9096652951128464901L;

	public CreditCardException(final String e) {
		super(e);
	}

}
