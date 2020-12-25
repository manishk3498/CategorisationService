package com.manish.categorization.rest.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategorizationResponse implements GenericResponse{

	@JsonProperty("cobrandId")
	private Long cobrandId;

	@JsonProperty("memId")
	private Long memId;

	@JsonProperty("txns")
	private List<TransactionResponse> txns = new ArrayList<TransactionResponse>();

	@JsonProperty("retry")
	private boolean retry;
	
	public Long getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}

	public Long getMemId() {
		return memId;
	}

	public void setMemId(Long memId) {
		this.memId = memId;
	}

	public List<TransactionResponse> getTxns() {
		return txns;
	}

	public void setTxns(List<TransactionResponse> txns) {
		this.txns = txns;
	}

	public void addTxn(TransactionResponse txn) {
		this.txns.add(txn);
	}
	public void addTxns(List<TransactionResponse> txnResList){
		this.txns.addAll(txnResList);
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}
	
}
