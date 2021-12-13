package com.tns.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard implements Serializable {
	private static final long serialVersionUID = 1L;

	private String number;

	private String expiration;

	private String securityCode;

	private String country;

	private String postalCode;
}
