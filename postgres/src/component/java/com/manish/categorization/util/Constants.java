package com.manish.categorization.util;

public class Constants {
	
	public static final String CNN_MERCHANT = "cnn_merchant";
	public static final String CNN_SUBTYPE = "cnn_subtype";
	public static final String CNN_CATEGORY = "cnn_category";
	public static final String BLOOM_FILTER = "bloom_filter";

	public static final Long ZERO_LONG = new Long(0);
	public static final Long ONE_LONG = new Long(1);
	public static final String EMPTY_STRING = "";
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String HYPHEN = "-";
	public static final String NULL_STRING = "null";
	
	public static final String BANK_DEBIT = "-";
	public static final String BANK_CREDIT = "-";
	public static final String CARD_DEBIT = "-";
	public static final String CARD_CREDIT = "-";
	public static final String STOCKS_DEBIT = "-";
	public static final String STOCKS_CREDIT = "-";
	public static final String INSURANCE_DEBIT = "-";
	public static final String INSURANCE_CREDIT = "-";
	public static final String LOANS_CREDIT = "-";
	public static final String LOANS_DEBIT = "-";
	
	public static final String CARD_CONTAINER = "card";
	
	public static final String SOURCE_SITE_LVL_SLAM_BANG_RULES = "4";
	public static final String SOURCE_USER_REGION_GLOBAL_SLAM_BANG_RULES = "21";
	public static final String SOURCE_SUM_INFO_PRIMARY_REGION_GLOBAL_SLAM_BANG_RULES = "22";
	public static final String SOURCE_GLOBAL_REGION_GLOABL_SLAM_BANG_RULES = "23";
	public static final String SOURCE_GLOBAL_LVL_SLAM_BANG_RULES = "5";
	public static final String GENERIC_MCC_RULES = "19";

	public static final String CC_PATTERN = "(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|"
			+ "(?:2131|1800|35\\d{3})\\d{11}|(?:4\\d{3}|5[1-5]\\d{2}|6011|7\\d{3})-?\\d{4}-?\\d{4}-?\\d{4}|3[4,7]\\d{13})";

	public static final String CATEGORIZATION_STATS = "categorizationStats";
	public static final String UNIQUE_TACKING_ID = "com.yodlee.ops.loggin.trackerId";
	public static final String TDE2_VERSION = "2";
	public static final String SIMPLE_DESC_VERSION = "1.1";
	public static final String VENDOR_NAME_VARIATION_TYPE = "3";
}
