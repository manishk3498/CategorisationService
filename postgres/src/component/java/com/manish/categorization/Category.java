package com.manish.categorization;
/**
 * 
 * @author mgarg
 *
 */
public enum Category {

	OTHER_EXPENSES("Other Expenses"),OTHER_INCOME("Other Income"),REFUNDS_ADJUSTMENTS("Refunds/Adjustments"),UNCATEGORIZED("Uncategorized");
	
	private final String categoryName;

	private Category(final String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return categoryName;
	}
}
