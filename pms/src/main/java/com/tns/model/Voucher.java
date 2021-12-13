package com.tns.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "Voucher")
@Getter
@Setter
@NoArgsConstructor
public class Voucher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column(nullable = false, unique = false)
	private String title;

	@Column
	@Size(max = 255)
	private String description;

	@Column
	private Date expiryDate;

	@Column()
	@Min(0)
	@Max(99999999)
	private double amount;

	@Column
	@Min(0)
	@Max(99999999)
	private int quantity;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PaymentMethod", referencedColumnName = "Id")
	private PaymentMethod paymentMethod;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VoucherItem", referencedColumnName = "Id")
	private VoucherItem voucherItem;

	@Column
	@Enumerated(EnumType.STRING)
	private EBuyType buyType;

	@Column
	@Size(max = 250)
	private String name;

	@Column
	@Size(max = 15)
	private String phoneNo;

	@Lob
	@Column
	private byte[] image;

	@Column(unique = true)
	@Size(min = 11, max = 11)
	private String promoCode;

	@Column(name = "paid")
	private boolean isPaid;

	@Column
	@Enumerated(EnumType.STRING)
	private VoucherStatus status;
}
