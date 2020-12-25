package com.manish.categorization.util;


import java.util.List;

import com.manish.categorization.db.ContainerBaseTypeRule;

public class CategorizationRules {
	private List<ContainerBaseTypeRule> rules;
	private String precision;

	public List<ContainerBaseTypeRule> getRules() {
		return rules;
	}

	public void setRules(List<ContainerBaseTypeRule> rules) {
		this.rules = rules;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

}

