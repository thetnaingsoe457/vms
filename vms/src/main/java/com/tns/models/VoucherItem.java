package com.tns.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "VoucherItem")
@Getter
@Setter
@NoArgsConstructor
public class VoucherItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column
	@Size(max = 255)
	private String name;

	@Column
	@Size(max = 500)
	private String description;

	@Column(name = "lmt_selt_qnt")
	private int limitedSelfQuantity;

	@Column(name = "lmt_other_qnt")
	private int limitedOtherQuantity;

	@Column
	@Min(0)
	@Max(365)
	private int expiryDay;

	@Column
	@Min(0)
	@Max(99999999)
	private long amountPerItem;

	@Column
	@Min(0)
	@Max(99999999)
	private long totalStock;

	@Column
	@Min(0)
	@Max(99999999)
	private long inStock;

	@Column
	@Min(0)
	@Max(99999999)
	private long outStock;

	@Column(name = "Active")
	private boolean isActive;
}
