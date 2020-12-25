package com.manish.categorization.algo.mcc;

import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
/**
 * 
 * @author mgarg
 *
 */
public interface MCCCategorizationService {
	
	public TransactionResponse categorise(TransactionRequest transactionRequest,CategorizationRequest request);
	
}
