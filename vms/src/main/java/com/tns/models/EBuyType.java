package com.tns.models;

public enum EBuyType {
	OnlyMeUsage("Only me usage"), GiftToOthers("Gift to others");

	private String name;

	EBuyType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
