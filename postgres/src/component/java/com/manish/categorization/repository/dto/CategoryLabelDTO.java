package com.manish.categorization.repository.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoryLabelDTO {
	private Long categoryId;
	private List<String> labels = new ArrayList<String>();

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void addLabel(String label) {
		this.labels.add(label);
	}
}
