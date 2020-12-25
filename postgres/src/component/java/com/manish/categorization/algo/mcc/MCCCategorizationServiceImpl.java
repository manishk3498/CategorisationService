package com.manish.categorization.algo.mcc;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manish.categorization.CategorizationSource;
import com.manish.categorization.Container;
import com.manish.categorization.db.GranularCategoryCache;
import com.manish.categorization.db.MccTxnCatMapCache;
import com.manish.categorization.db.TransactionCategory;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.EnrichedTransactionResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.util.CommonHelper;
import com.yodlee.simpledescservice.rest.dto.SimpleDescriptionRequest;
import com.yodlee.simpledescservice.service.SimpleDescriptionService;
import com.yodlee.simpledescservice.service.SimpleDescriptionServiceImpl;
/**
 * 
 * @author mgarg
 *
 */
@Service
public class MCCCategorizationServiceImpl implements MCCCategorizationService{

	@Autowired
	MccTxnCatMapCache mccTxnCatMapCache;
	
	@Autowired
	TransactionCategoryCache categoryCache;
	
	@Autowired
	GranularCategoryCache granularCategoryCache;
	
	public TransactionResponse categorise(TransactionRequest txnRequest,CategorizationRequest request){
		TransactionResponse response = null;
		boolean mergerEnabled = (request.getConfigurations() != null && request.getConfigurations().isMergerEnabled() == true) ? Boolean.TRUE : Boolean.FALSE;
		boolean tdeEnabled = (request.getConfigurations() != null && request.getConfigurations().isMeerkat() == true) ? Boolean.TRUE : Boolean.FALSE;
		String transactionCategoryName = applyMCCCategorization(txnRequest.getMccCode());
		TransactionCategory category = categoryCache.getTransactionCategory(transactionCategoryName,
				mergerEnabled);
		if(category != null){
			EnrichedTransactionResponse txnResponse = new EnrichedTransactionResponse();	
			txnResponse.setCategorisationSource(CategorizationSource.MCC.getSource());
			txnResponse.setTransactionId(txnRequest.getTransactionId());
			txnResponse.setCategorisationSourceId(CategorizationSource.MCC.getSourceId());
			if(mergerEnabled){
				txnResponse.setCategory(category.getMergedTxnCatName());
			}else{
				txnResponse.setCategory(category.getName());
			}
			if (Container.BANK.toString().equalsIgnoreCase(request.getContainer())
					|| Container.CARD.toString().equalsIgnoreCase(request.getContainer())
					|| Container.LOAN.toString().equalsIgnoreCase(request.getContainer())
					|| Container.INVESTMENT.toString().equalsIgnoreCase(request.getContainer())) {
				String granularCategory = CommonHelper.getDefaultGranularCategory(granularCategoryCache, mergerEnabled,
						tdeEnabled, txnResponse);
				txnResponse.setGranularCategory(granularCategory);
			}
			if(Container.INVESTMENT.toString().equalsIgnoreCase(request.getContainer()) 
					&& request.getConfigurations().isMeerkat()
					&& request.isTdeV2() && StringUtils.isEmpty(txnResponse.getSimpleDescription())){
				boolean isSimpleDescEnabled=request.getConfigurations().isSimpleDescEnabled();
				SimpleDescriptionRequest simpleDescRequest = new SimpleDescriptionRequest();
				if(isSimpleDescEnabled){
					SimpleDescriptionService simpleDescriptionService = new SimpleDescriptionServiceImpl();
					BeanUtils.copyProperties(txnResponse, simpleDescRequest);
					simpleDescRequest.setVersion(request.getConfigurations().getSimpleDescVersion());
					simpleDescRequest.setGeoLocationEnabledInSD(request.getConfigurations().isGeoLocationEnabledInSD());
					simpleDescRequest.setPlainTextDesc(txnRequest.getDescription());
					simpleDescRequest.setLocale(request.getConfigurations().getLocale());
					simpleDescRequest.setContainer(request.getContainer());
					simpleDescRequest.setTxnId(txnRequest.getTransactionId().toString());
					simpleDescRequest.setCategory(txnResponse.getCategory());
					simpleDescRequest.setTxnBaseType(txnRequest.getBaseType());
					txnResponse.setSimpleDescription(simpleDescriptionService.deriveSimpleDescription(simpleDescRequest));
				}
			}
			response = txnResponse;
		}
		return response;
	}
	public String applyMCCCategorization(Long mccCode){
		
		String transactionCategory = null;
		Map<Long,String> mccTxnCatMap = mccTxnCatMapCache.getMccTxnCatMap();
		if(mccTxnCatMap != null){
			transactionCategory = mccTxnCatMap.get(mccCode);
		}
		return transactionCategory;
	}
	
}
