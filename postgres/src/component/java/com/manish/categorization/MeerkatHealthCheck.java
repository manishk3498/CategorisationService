package com.manish.categorization;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manish.categorization.algo.tde.dto.MeerkatRequest;
import com.manish.categorization.algo.tde.dto.MeerkatRequest.TransactionReq;
import com.manish.categorization.http.FluentExecutor;
import com.manish.categorization.util.Constants;
/**
 * This class is used to do health check for requested Meerkat URL   
 * @author mdey
 *
 */
public class MeerkatHealthCheck
{
	
	private static final Logger logger = LogManager.getLogger(MeerkatHealthCheck.class);
	/**
	 * Prepare sample request for Meerkat service
	 * @return	MeerkatRequest
	 */
	public static MeerkatRequest getSampleMeerkatRequest() {
		MeerkatRequest request = new MeerkatRequest();
		request.setCobrandId(10000004L);
		request.setMemId(1000000001L);
		request.setContainer(Container.BANK.toString());
		request.setRegion(1);
		List<String> services = new ArrayList<String>();
		services.add(Constants.CNN_MERCHANT.toString());
		services.add(Constants.CNN_SUBTYPE.toString());
		services.add(Constants.BLOOM_FILTER.toString());
		request.setServices(services);
		List<TransactionReq> txnRequestList = new ArrayList<TransactionReq>();
		TransactionReq txnRequest = new TransactionReq();
		txnRequest.setAmount(new Double(12.0));
		txnRequest.setDate("14/06/2018");
		txnRequest.setTransactionId(new BigInteger("1"));
		txnRequest.setDescription("interest income");
		txnRequest.setLedgerEntry("debit");
		txnRequestList.add(txnRequest);
		request.setTxns(txnRequestList);
		return request;
	}
	
	/**
	 * Do health check for Region specific Meerkat URL
	 * @param tdeUrlList	List<String>
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void healthCheck(Map<Integer, String> regionMap){
		
		MeerkatRequest meerkatRequest = getSampleMeerkatRequest();
		ObjectMapper mapper = new ObjectMapper();
		String meerkatRequestJson = null;
		try {
			meerkatRequestJson = mapper.writeValueAsString(meerkatRequest);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		Executor executor = FluentExecutor.getExecutor();
		Iterator<Entry<Integer, String>> regionIterator = regionMap.entrySet().iterator();
		if(null != regionMap){
			while(regionIterator.hasNext())
			{
			    Entry<Integer, String> next = regionIterator.next();
			    Integer region = next.getKey();
			    String tdeUrl = next.getValue();
			    Response response = null;
			    
			    if(!StringUtils.isEmpty(tdeUrl)){
			    	try {
						
						response = executor.execute(Request.Post(tdeUrl).connectTimeout(2000)
								.socketTimeout(2000)
								.bodyString(meerkatRequestJson, ContentType.APPLICATION_JSON));
					} catch (ClientProtocolException e) {
						logger.error("DAAS OPS Alert: CilentProtocolException during health check of TDE URL:"+ tdeUrl+" during startup +", e);
					} catch (IOException e) {
						logger.error("DAAS OPS Alert: IOException during health check of TDE URL:"+ tdeUrl+" during startup +", e);
					} catch (Exception e) {
						logger.error("DAAS OPS Alert: Exception during health check of TDE URL:"+ tdeUrl+" during startup +", e);
					}
					org.apache.http.HttpResponse httpResponse = null;
					try {
						httpResponse = response != null ? response.returnResponse() : null;
					} catch (IOException e) {
						logger.error(" DAAS OPS Alert: IOException while fetching the response +", e);
					}
					int rc = (httpResponse != null && httpResponse.getStatusLine() != null) ? httpResponse.getStatusLine().getStatusCode() : 0;
					if (rc == org.apache.http.HttpStatus.SC_OK) {
						logger.info("URL Health Check Success for region Id: "+region +", Meerkat URL : "+tdeUrl);
						System.out.println("URL Health Check Success for region Id: "+region +", Meerkat URL : "+tdeUrl);
					}else{
						logger.info("DAAS OPS Alert: URL Health Check failed for region Id: "+region +", Meerkat URL : "+tdeUrl);
						System.err.print("DAAS OPS Alert: URL Health Check failed for region Id: "+region +", Meerkat URL : "+tdeUrl);
						System.err.print("DAAS OPS Alert: Response code for URL Health Check" + rc );
						logger.info("DAAS OPS Alert: Response code for URL Health Check" + rc);
						System.err.print("DAAS OPS Alert: TDE URL:"+ tdeUrl+" mentioned in the config file are not reachable");
						logger.info("DAAS OPS Alert: TDE URL:"+ tdeUrl+" mentioned in the config file are not reachable");
						//throw new CategorizationException("TDE URL mentioned in the properties file are not reachable");
					}
			    }else{
			    	logger.info("DAAS OPS Alert: TDE URL empty for region Id: "+region +", Meerkat URL : "+tdeUrl);
			    	System.err.print("DAAS OPS Alert:  TDE URL empty for region Id: "+region +", Meerkat URL : "+tdeUrl);
			    }
			}
		}
	}
}