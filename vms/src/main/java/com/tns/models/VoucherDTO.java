package com.tns.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VoucherDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String title;

	private String description;

	private Date expiryDate;

	private double amount;

	private int quantity;

	private Long voucherItemId;

	@Enumerated(EnumType.STRING)
	private EBuyType buyType;

	private String name;

	private String phoneNo;

	private boolean isPaid;

	@Enumerated(EnumType.STRING)
	private VoucherStatus status;
}
