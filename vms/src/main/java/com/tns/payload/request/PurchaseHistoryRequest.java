package com.tns.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseHistoryRequest {
	@NotBlank
	private String phoneNo;
}
