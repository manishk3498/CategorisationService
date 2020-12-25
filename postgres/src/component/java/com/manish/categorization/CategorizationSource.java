package com.manish.categorization;

public enum CategorizationSource {
	MEERKAT("MEERKAT","24"), LEGACY("LEGACY","23"), MCC("MCC","19"), INVESTMENT_TRANSACTION_TYPE_MAPPING(
			"INVESTMENT_TRANSACTION_TYPE_MAPPING","15"), NONE("NONE","0");

	private final String source;
	private final String sourceId;
	private CategorizationSource(final String source,final String sourceId) {
		this.source = source;
		this.sourceId = sourceId;
	}

	@Override
	public String toString() {
		return source;
	}
	public String getSource(){
		return source;
	}
	public String getSourceId(){
		return sourceId;
	}
}
