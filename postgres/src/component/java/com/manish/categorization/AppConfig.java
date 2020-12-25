package com.manish.categorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.manish.categorization.rest.dto.TransactionResponse;
import com.manish.categorization.sdp.config.CaasConfigBean;

@Configuration
public class AppConfig
{	 
	private static final Logger logger = LogManager.getLogger(AppConfig.class);
	@Autowired
	CaasConfigBean caasConfigBean;
	
	/*@Bean
    @ConfigurationProperties(prefix = "custom.rest.connection")
    public HttpComponentsClientHttpRequestFactory customHttpRequestFactory() 
    {
        return new HttpComponentsClientHttpRequestFactory();
    }

    @Bean
    public RestTemplate customRestTemplate()
    {
        return new RestTemplate(customHttpRequestFactory());
    }*/

	/*@Bean
	public RestTemplate customRestTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(caasConfigBean.getCaas().getCustomRestRequestTimeout());
		httpRequestFactory.setConnectTimeout(caasConfigBean.getCaas().getCustomRestConnectionTimeout());
		httpRequestFactory.setReadTimeout(caasConfigBean.getCaas().getCustomRestReadTimeout());
		return new RestTemplate(httpRequestFactory);
	}
	*/
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) 
    {
        return restTemplateBuilder
           .setConnectTimeout(caasConfigBean.getCaas().getCustomRestConnectionTimeout())
           .setReadTimeout(caasConfigBean.getCaas().getCustomRestReadTimeout())
           .build();
    }
    
    @Bean
    public Directory luceneDirectory(){
    	return new RAMDirectory();
    }
    @Bean
    public ExecutorCompletionService<List<TransactionResponse>> meerkatExecutorCompletionService(){
		ArrayBlockingQueue<Runnable> meerkatThreadQueue = new ArrayBlockingQueue<Runnable>(caasConfigBean.getCaas().getQueueThreadsLimit());
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(caasConfigBean.getCaas().getNumberOfMeerkatThreads(),
				caasConfigBean.getCaas().getNumberOfMeerkatThreads(), 5000, TimeUnit.SECONDS, meerkatThreadQueue);
		return new ExecutorCompletionService<List<TransactionResponse>>(threadPoolExecutor);
    }
    @Bean
    public ExecutorCompletionService<List<TransactionResponse>> legacyExecutorCompletionService(){
    	//ExecutorService executorService1 = Executors.newFixedThreadPool(numberOfLegacyThreads);
		ArrayBlockingQueue<Runnable> legacyThreadQueue = new ArrayBlockingQueue<Runnable>(caasConfigBean.getCaas().getQueueThreadsLimit());
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(caasConfigBean.getCaas().getNumberOfLegacyThreads(),
				caasConfigBean.getCaas().getNumberOfLegacyThreads(), 5000, TimeUnit.SECONDS, legacyThreadQueue);
		 return new ExecutorCompletionService<List<TransactionResponse>>(threadPoolExecutor);
    }
    @Bean
    public HttpHost httphost(){
    	
    	HttpHost httpHost = new HttpHost(caasConfigBean.getCaas().getProxyHost(), caasConfigBean.getCaas().getProxyPort());
    	return httpHost;
    }
    @Bean
    public URLConfig urlConfig(){
    	
    	Map<Integer, String> regionMap = new HashMap<>();
    	regionMap.put(1, caasConfigBean.getCaas().getMeerkatUrlRegion1());
    	regionMap.put(2, caasConfigBean.getCaas().getMeerkatUrlRegion2());
    	regionMap.put(3, caasConfigBean.getCaas().getMeerkatUrlRegion3());
    	regionMap.put(4, caasConfigBean.getCaas().getMeerkatUrlRegion4());
    	regionMap.put(7, caasConfigBean.getCaas().getMeerkatUrlRegion7());
    	URLConfig urlConfig = new URLConfig(regionMap);
    	return urlConfig;
    }
    
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        if(StringUtils.isEmpty(caasConfigBean.getCaas().getSpringDataSourceDriverClassName())) {
        	logger.error("DAAS OPS Alert: Spring data source driver is null");
        }
        if(StringUtils.isEmpty(caasConfigBean.getCaas().getSpringDataSourceUrl())) {
        	logger.error("DAAS OPS Alert: Spring data source url is null");
        }
        if(StringUtils.isEmpty(caasConfigBean.getCaas().getSpringDataSourceUserName())) {
        	logger.error("DAAS OPS Alert: Spring data source username is null");
        }
        if(StringUtils.isEmpty(caasConfigBean.getCaas().getSpringDataSourcePassword())) {
        	logger.error("DAAS OPS Alert: Spring data source password is null");
        }
        dataSourceBuilder.driverClassName(caasConfigBean.getCaas().getSpringDataSourceDriverClassName());
        dataSourceBuilder.url(caasConfigBean.getCaas().getSpringDataSourceUrl());
        dataSourceBuilder.username(caasConfigBean.getCaas().getSpringDataSourceUserName());
        dataSourceBuilder.password(caasConfigBean.getCaas().getSpringDataSourcePassword());
        return dataSourceBuilder.build();
    }
 
}