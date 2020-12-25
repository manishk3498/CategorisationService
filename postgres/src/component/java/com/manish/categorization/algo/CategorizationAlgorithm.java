package com.manish.categorization.algo;

import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.CategorizationResponse;
import com.manish.categorization.util.YCategorizationStats;

public interface CategorizationAlgorithm {
	CategorizationResponse categorise(CategorizationRequest request,YCategorizationStats categorizationStats);
}
