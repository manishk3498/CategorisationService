package com.manish.categorization;

/**
 * 
 * @author mgarg
 *
 */
public enum BaseType {

	DEBIT("debit"),CREDIT("credit"),OTHER("other"),UNKNOWN("unknown");
	
	public final String baseType;
	
	private BaseType(final String baseType) {
		this.baseType = baseType;
	}
	@Override
	public String toString() {
		return baseType;
	}
}
