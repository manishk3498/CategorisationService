package com.manish.categorization.algo.tde.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.manish.categorization.Container;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.Configurations;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.util.Constants;

/*
 {
  "cobrand_id": 1234,
  "user_id": 2345,
  "transaction_list": [
    {
      "transaction_id": 1588389189,
      "description": "",
      "amount": 374.4,
      "date": "4/16/2016",
      "ledger_entry": "debit"
    }
  ],
  "container": "bank",
  "debug": true,
  "services_list": [
    "cnn_subtype",
    "search"
  ],
  "user_country": "CA",
  "user_city": "San Francisco",
  "cobrand_region": 1
} 
*/
public class MeerkatRequest implements Serializable {

	@JsonProperty(value="cobrand_id", required = true)
	@SerializedName("cobrand_id")
	private long cobrandId;

	@JsonProperty(value="user_id", required = true)
	@SerializedName("user_id")
	private long memId;

	@JsonProperty(value="transaction_list", required = true)
	@SerializedName("transaction_list")
	private List<TransactionReq> txns = new ArrayList<MeerkatRequest.TransactionReq>();

	@JsonProperty(value="container", required = true)
	@SerializedName("container")
	private String container;

	@JsonProperty(value = "debug", required = false, defaultValue = "false")
	@SerializedName("debug")
	private boolean debug;

	@JsonProperty("services_list")
	@SerializedName("services_list")
	private List<String> services;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("user_country")
	@SerializedName("user_country")
	private String country;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("user_state")
	@SerializedName("user_state")
	private String state;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("user_city")
	@SerializedName("user_city")
	private String city;

	@JsonProperty(value = "cobrand_region", required = true, defaultValue = "1")
	@SerializedName("cobrand_region")
	private int region;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("account_classification")
	@SerializedName("account_classification")
	private String accountClassification;
	
	@JsonProperty(value = "is_small_business", required = false, defaultValue = "false")
	@SerializedName("is_small_business")
	private boolean smallBusiness;

	public MeerkatRequest() {
		super();
	}

	public MeerkatRequest(CategorizationRequest request, List<String> defaultServicesList) {
		super();
		if (!StringUtils.isEmpty(request.getCobrandId())) {
			this.cobrandId = request.getCobrandId();
		}
		if (!StringUtils.isEmpty(request.getMemId())) {
			this.memId = request.getMemId();
		}
		if (!StringUtils.isEmpty(request.getContainer())) {
			//Kind of hack - Meerkat accepts card string for Credits container which is called Credits in platform layer
			this.container = request.getContainer();
			if(container.equals(Container.CARD.toString())){
				this.container = Constants.CARD_CONTAINER;
			}
		}
		if (!StringUtils.isEmpty(request.getRegion())) {
			this.region = request.getRegion().intValue();
		}
		if (!StringUtils.isEmpty(request.getCountry())) {
			this.country = request.getCountry();
		}
		if (!StringUtils.isEmpty(request.getCity())) {
			this.city = request.getCity();
		}
		if (!StringUtils.isEmpty(request.getState())) {
			this.state = request.getState();
		}
		
		if (!StringUtils.isEmpty(request.getAccountClassification())) {
			this.accountClassification = request.getAccountClassification();
		}
		this.smallBusiness = request.isSmallBusiness();	
		Configurations configurations = request.getConfigurations();
		if(configurations != null)
			this.services = configurations.getServices();
		if(services == null){
			this.services = defaultServicesList;
		}
		
		for (TransactionRequest txnReq : request.getTxns()) {
			txns.add(new TransactionReq(txnReq));
		}
		if(configurations != null)
			this.debug = configurations.isDebug();
	}


	public void addAllTxns(List<TransactionRequest> txns) {
		for (TransactionRequest txnReq : txns) {
			this.txns.add(new TransactionReq(txnReq));
		}
	}

	public long getCobrandId() {
		return cobrandId;
	}

	public void setCobrandId(long cobrandId) {
		this.cobrandId = cobrandId;
	}

	public long getMemId() {
		return memId;
	}

	public void setMemId(long memId) {
		this.memId = memId;
	}

	public List<TransactionReq> getTxns() {
		return txns;
	}

	public void setTxns(List<TransactionReq> txns) {
		this.txns = txns;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public int getRegion() {
		return region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setRegion(int region) {
		this.region = region;
	}	

	public String getAccountClassification() {
		return accountClassification;
	}

	public void setAccountClassification(String accountClassification) {
		this.accountClassification = accountClassification;
	}

	public boolean isSmallBusiness() {
		return smallBusiness;
	}

	public void setSmallBusiness(boolean smallBusiness) {
		this.smallBusiness = smallBusiness;
	}






	public static class TransactionReq implements Serializable {
		/*SimpleDateFormat sdfmt1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfmt2 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdfmt3= new SimpleDateFormat("dd-MMM-yyyy");*/
		
		@JsonProperty("transaction_id")
		@SerializedName("transaction_id")
		private BigInteger transactionId;

		@JsonProperty("description")
		@SerializedName("description")
		private String description;

		@JsonProperty(value = "amount", required = false)
		@SerializedName("amount")
		private double amount;

		@JsonProperty(value = "date", required = false)
		@SerializedName("date")
		private String date;

		@JsonProperty("ledger_entry")
		@SerializedName("ledger_entry")
		private String ledgerEntry;
		
		@JsonInclude(JsonInclude.Include.NON_NULL)
		@JsonProperty("account_type")
		@SerializedName("account_type")
		private String accountType;

		public TransactionReq(TransactionRequest request) {
			super();
			this.transactionId = request.getTransactionId();
			this.description = request.getDescription();
			this.amount = request.getAmount() != null ? request.getAmount().doubleValue() : 0.0;
			this.date = request.getDate();
			if(this.date == null) {
				this.date = Constants.EMPTY_STRING;
			}
			/*try {
	            java.util.Date dDate = sdfmt1.parse(request.getDate());
	    		this.date = sdfmt1.format( dDate );
	        } catch (ParseException e) {
	        	try {
	        		java.util.Date dDate = sdfmt2.parse(request.getDate());
		    		this.date = sdfmt2.format( dDate );
				} catch (ParseException e1) {
					try {
						java.util.Date dDate = sdfmt3.parse(request.getDate());
			    		this.date = sdfmt3.format( dDate );
					} catch (ParseException e2) {
						this.date=sdfmt1.format(new Date());
					}
				}

			}*/
			this.ledgerEntry = request.getBaseType();
			this.accountType = request.getAccountType();
		}

		public TransactionReq() {
			super();
		}

		public BigInteger getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(BigInteger transactionId) {
			this.transactionId = transactionId;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getLedgerEntry() {
			return ledgerEntry;
		}

		public void setLedgerEntry(String ledgerEntry) {
			this.ledgerEntry = ledgerEntry;
		}

		public String getAccountType() {
			return accountType;
		}

		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		
	}
}
