package com.tns.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDTO {
	private double amount;
	private double discountAmount;
	private double paidAmount;

	private Long paymentMethodId;
	private Long voucherId;

	private String cardNumber;
	private String expiration;
	private String securityCode;
	private String country;
	private String postalCode;
}
