package com.tns.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PaymentMethod")
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column(name = "Name", nullable = false, unique = true)
	private String name;

	@Column
	@Size(max = 255)
	private String description;

	@Column
	@Size(max = 100)
	private double discountPercentage;

	@Column(name = "Active")
	private boolean isActive;

	public PaymentMethod(String name, @Size(max = 255) String description, @Size(max = 100) double discountPercentage,
			boolean isActive) {
		super();
		this.name = name;
		this.description = description;
		this.discountPercentage = discountPercentage;
		this.isActive = isActive;
	}

}
