package com.manish.categorization;
/**
 * 
 * @author mgarg
 *
 */
public enum Container {

	BANK("bank"),CARD("credits"),INVESTMENT("stocks"),LOAN("loans"),INSURANCE("insurance");
	
	private String container;
	
	private Container(String container) {
		this.container = container;
	}
	@Override
	public String toString() {
		return container;
	}
}
