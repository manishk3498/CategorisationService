package com.manish.categorization.algo.legacy;

import java.util.List;

import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;

public interface LegacyCategoisationService {
	public List<TransactionResponse> categorise(CategorizationRequest request);
	
	public TransactionResponse categorise(CategorizationRequest request, TransactionRequest txn);
}
