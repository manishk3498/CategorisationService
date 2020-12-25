package com.manish.categorization.rest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.manish.categorization.BaseType;
import com.manish.categorization.Container;
import com.manish.categorization.algo.CategorizationAlgorithm;
import com.manish.categorization.rest.dto.CategorizationRequest;
import com.manish.categorization.rest.dto.CategorizationResponse;
import com.manish.categorization.rest.dto.Configurations;
import com.manish.categorization.rest.dto.ExceptionResponse;
import com.manish.categorization.rest.dto.GenericResponse;
import com.manish.categorization.rest.dto.TransactionRequest;
import com.manish.categorization.util.Constants;
import com.manish.categorization.util.YCategorizationStats;

@RestController
class CategorizationController {

	private static final Logger logger = LogManager.getLogger(CategorizationController.class);
	
	@Autowired
	private CategorizationAlgorithm algorithm;

	@RequestMapping(path = "/categorise", method = RequestMethod.POST)
	ResponseEntity<GenericResponse> categorise(@RequestBody String json,HttpServletRequest httpRequest,HttpServletResponse httpResponse) {
		
		Gson gson = new GsonBuilder().create();
		CategorizationRequest request = null;
		try {		
			request = gson.fromJson(json, CategorizationRequest.class);
			if (request == null) {
				logger.error("Categoriztaion Request is null");
			} else {
				ResponseEntity<GenericResponse> response = validateRequest(request);
				if(response!=null){
					if(response.getStatusCode()==HttpStatus.BAD_REQUEST) {
						return response;
					}
				}
			}
		}catch(Exception e){
			return new ResponseEntity<GenericResponse>(HttpStatus.BAD_REQUEST);
		}
		logger.info("Started CAAS Processing : Unique Txn Id::"+ request.getUniqueTrackingId());
		YCategorizationStats categorizationStats = new YCategorizationStats();
		categorizationStats.setTxnsCount(request.getTxns().size());
		categorizationStats.setCobrandId(request.getCobrandId());
		categorizationStats.setRegionId(request.getRegion());
		String uniqueTrackingId = request.getUniqueTrackingId();
		if(uniqueTrackingId == null){
			uniqueTrackingId = Constants.EMPTY_STRING;
		}
		categorizationStats.setUniqueTrackingId(request.getUniqueTrackingId());
		CategorizationResponse response = algorithm.categorise(request,categorizationStats);
		httpRequest.setAttribute(Constants.CATEGORIZATION_STATS,categorizationStats.toString());
		httpRequest.setAttribute(Constants.UNIQUE_TACKING_ID,categorizationStats.getUniqueTrackingId());
		logger.info("Completed CAAS Processing : Unique Txn Id::"+ request.getUniqueTrackingId() + " , Txn Count:"+ request.getTxns().size());
		
		return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
	}
	
	
	/**
	 * This end point is used for testing the categorisation service status
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping(path = "/pingcategorise", method = RequestMethod.GET)
	ResponseEntity<GenericResponse> pingCategorise(HttpServletRequest httpRequest,HttpServletResponse httpResponse) {
	
		long startTime = System.currentTimeMillis();
		CategorizationRequest request = new CategorizationRequest();
		request.setCobrandId(10000004L);
		request.setMemId(1000000001L);
		request.setContainer(Container.BANK.toString());
		request.setRegion(1L);
		request.setSumInfoId(11195L);
		Configurations configuration = new Configurations();
		configuration.setGeoLocationEnabledInSD(Boolean.TRUE);
		configuration.setLegacy(Boolean.TRUE);
		configuration.setMeerkat(Boolean.FALSE);
		configuration.setMergerEnabled(Boolean.TRUE);
		configuration.setSimpleDescEnabled(Boolean.TRUE);
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		configuration.setServices(services);
		request.setConfigurations(configuration);
		List<TransactionRequest> txnRequestList = new ArrayList<TransactionRequest>();
		TransactionRequest txnRequest = new TransactionRequest();
		txnRequest.setAmount(new Double(12.0));
		txnRequest.setBaseType(BaseType.DEBIT.toString());
		txnRequest.setDate("12/01/2018");
		txnRequest.setTransactionId(new BigInteger("1"));
		txnRequest.setDescription("WINCOSIN CHILDREN'S HOSPITAL");
		txnRequestList.add(txnRequest);
		request.setTxns(txnRequestList);
		YCategorizationStats categorizationStats = new YCategorizationStats();
		categorizationStats.setTxnsCount(request.getTxns().size());
		categorizationStats.setCobrandId(request.getCobrandId());
		categorizationStats.setRegionId(request.getRegion());//region id
		//For ping request we are generating dummy uniqueTrackingId
		categorizationStats.setUniqueTrackingId(UUID.randomUUID().toString());
		CategorizationResponse response = algorithm.categorise(request,categorizationStats);
		if(response==null) {
			logger.error("CategorizationResponse is null for sample request : "+ request +" for Unique Txn ID:" +categorizationStats.getUniqueTrackingId());
		}
		httpRequest.setAttribute(Constants.CATEGORIZATION_STATS,categorizationStats.toString());
		httpRequest.setAttribute(Constants.UNIQUE_TACKING_ID,categorizationStats.getUniqueTrackingId());
		long endTime = System.currentTimeMillis();
		long totalTimeTaken = endTime - startTime;
		logger.info("Total time taken for ping request in milliseconds "+totalTimeTaken);
				
		return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
	}
	private boolean isValidContainer(String container){
		boolean isValid = Boolean.FALSE;
		if(StringUtils.isNotEmpty(container)){
			if(Container.BANK.toString().equals(container) || Container.CARD.toString().equals(container) || Container.INVESTMENT.toString().equals(container)
				|| Container.INSURANCE.toString().equals(container) || Container.LOAN.toString().equals(container)){
				isValid = Boolean.TRUE;
			}
		}
		return isValid;
	}
	
	private  ResponseEntity<GenericResponse> validateRequest(CategorizationRequest request) {
		
		CategorizationResponse response =null;
		String exceptionStr = null;
		if(StringUtils.isEmpty(String.valueOf(request.getCobrandId()))){
			exceptionStr="Please verify - Cobrand Id must not be blank and allow only numeric.";
		}
		if(StringUtils.isEmpty(String.valueOf(request.getMemId()))){
			exceptionStr= "Please verify - User Id must not be blank and allow numeric only.";
		}
		if(StringUtils.isEmpty(String.valueOf(request.getSumInfoId()))){
			exceptionStr ="Please verify - Suminfo Id must not be blank and allow numeric only.";
		}
		if(StringUtils.isEmpty(String.valueOf(request.getContainer())) || !isValidContainer(request.getContainer())){
			exceptionStr = "Please pass a valid container";
		}
		if(StringUtils.isEmpty(String.valueOf(request.getRegion()))){
			exceptionStr= "Region must not be blank and allow numeric only.";
		}
		if(request.getTxns() == null  || request.getTxns().size() == 0){
			exceptionStr = "Transaction List should not be empty.";
		}
		if(request.getConfigurations() == null){
			exceptionStr= "Configurations in CategorisationRequest cannot be null" ;			
		}		
		if (StringUtils.isNotEmpty(exceptionStr)) {
			ExceptionResponse exceptionResponse = new ExceptionResponse();
			exceptionResponse.setErrorMessage(exceptionStr);
			return new ResponseEntity<GenericResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
		}else {
			return  new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
		}
	}
}
