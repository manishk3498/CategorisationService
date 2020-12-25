package com.manish.categorization.algo.tde;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.CategorizationResponse;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.util.YCategorizationStats;

public interface MeerkatService {
	
	List<TransactionResponse> categorise(CategorizationRequest request,
			CategorizationResponse response,
			Map<BigInteger, EnrichedTransactionResponse> mccTxnCatIdToResponseMap,YCategorizationStats categorizationStats);

}