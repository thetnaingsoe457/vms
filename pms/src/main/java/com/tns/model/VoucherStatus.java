package com.tns.model;

public enum VoucherStatus {
	Submitted("Submitted"), Paid("Paid"), Used("Used"), Active("Active"), Deactivated("Deactivated");

	private String name;

	VoucherStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
