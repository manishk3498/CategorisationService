package com.manish.categorization.db;

import java.io.Serializable;
import java.util.List;

import org.apache.lucene.document.Document;

import com.manish.categorization.util.Constants;

public class SlamBangWord implements Serializable, Comparable<SlamBangWord> {
	private Long slamBangWordId;
	private String slamBangWord;
	private Long merchantId;
	private String merchantName;
	private Long sumInfoId;
	private String transactionCategoryName;
	private String transactionType;
	private Long regionId;
	private Long priority;
	private int order = -1;
	private String source;
	private Long isDummyMerchant;
	private boolean isDeleted;
	private String containers;
	private List<String> containerList;
	public SlamBangWord() {
	}

	public SlamBangWord(Document document) {
		super();
		this.slamBangWordId = getLongProperty(document, "slamBangWordId");
		this.slamBangWord = document.get("slamBangWord");
		this.merchantId = getLongProperty(document, "merchantId");
		this.merchantName = document.get("merchant");
		this.sumInfoId = getLongProperty(document, "sumInfoId");
		this.transactionCategoryName = document.get("transactionCategoryName");
		this.transactionType = document.get("transactionType");
		this.regionId = getLongProperty(document, "regionId");
		this.priority = getLongProperty(document, "priority");
		this.isDummyMerchant = getLongProperty(document, "isDummy");
	}


	public Long getSlamBangWordId() {
		return slamBangWordId;
	}


	public void setSlamBangWordId(Long slamBangWordId) {
		this.slamBangWordId = slamBangWordId;
	}


	public String getSlamBangWord() {
		return slamBangWord;
	}


	public void setSlamBangWord(String slamBangWord) {
		this.slamBangWord = slamBangWord;
	}


	public Long getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}


	public Long getSumInfoId() {
		return sumInfoId;
	}


	public void setSumInfoId(Long sumInfoId) {
		this.sumInfoId = sumInfoId;
	}


	public String getTransactionCategoryName() {
		return transactionCategoryName;
	}


	public void setTransactionCategoryName(String transactionCategoryName) {
		this.transactionCategoryName = transactionCategoryName;
	}


	public String getTransactionType() {
		return transactionType;
	}


	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}


	public Long getRegionId() {
		return regionId;
	}


	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}


	public Long getPriority() {
		return priority;
	}


	public void setPriority(Long priority) {
		this.priority = priority;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}
	
	public Long getIsDummyMerchant() {
		return isDummyMerchant;
	}

	public void setIsDummyMerchant(Long isDummyMerchant) {
		this.isDummyMerchant = isDummyMerchant;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getContainers() {
		return containers;
	}
	public void setContainers(String containers) {
		this.containers = containers;
	}
	
	public List<String> getContainerList() {
		return containerList;
	}

	public void setContainerList(List<String> containerList) {
		this.containerList = containerList;
	}

	private Long getLongProperty(Document document, String name) {
		String value = document.get(name);
		if (value != null && !value.trim().isEmpty()) {
			return Long.parseLong(value);
		} else {
			return null;
		}
	}

	@Override
	public int compareTo(SlamBangWord slamBangWordObj) {
		int priorityT = -1;
		priorityT = (order < slamBangWordObj.order ? -1 : (order == slamBangWordObj.order ? 0 : 1));
		// COMPARING BY PRIORITY
		if (priorityT == 0) {
			Long priorityObj1 = (null == priority || (priority.equals(Constants.ZERO_LONG))) ? Constants.ONE_LONG
					: priority;
			Long priorityObj2 = (null == slamBangWordObj.priority
					|| (slamBangWordObj.priority.equals(Constants.ZERO_LONG))) ? Constants.ONE_LONG
							: slamBangWordObj.priority;
			priorityT = priorityObj1.compareTo(priorityObj2);
		}

		if (priorityT == 0) {
			if (slamBangWord.length() > 0 && slamBangWordObj.slamBangWord.length() > 0) {
				priorityT = (slamBangWord.length() > slamBangWordObj.slamBangWord.length() ? -1
						: (slamBangWord.length() == slamBangWordObj.slamBangWord.length() ? 0 : 1));
			} else if (slamBangWord.length() < 0) {
				priorityT = -1;
			} else if (slamBangWordObj.slamBangWord.length() < 0) {
				priorityT = 1;
			}
		}
		return priorityT;
	}
}
