package com.tns.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	private double amount;
	private double discountAmount;
	private double paidAmount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PaymentMethod", referencedColumnName = "Id")
	private PaymentMethod paymentMethod;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Voucher", referencedColumnName = "Id")
	private Voucher voucher;

	private String cardNumber;
	private String expiration;
	private String securityCode;
	private String country;
	private String postalCode;
}
