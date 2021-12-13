package com.tns.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistoryVoucherResponse {
	private String status;
	private byte[] qrCode;
	private String promoCode;
}
