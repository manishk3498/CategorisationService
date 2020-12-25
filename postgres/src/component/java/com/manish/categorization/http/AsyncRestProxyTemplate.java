package com.manish.categorization.http;
/*

package com.manish.categorization.http;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;

@Component
public final class AsyncRestProxyTemplate {

	

	@Autowired
	AsyncRestTemplate restTemplate;

	@Value("${proxy.host}")
	String host;

	@Value("${proxy.port}")
	Integer port;
	
	@Value("${maximum.connections}")
	String maximumConnections;
	
	private static final Logger logger = LogManager.getLogger(AsyncRestProxyTemplate.class);
	
	@PostConstruct
	public void init() {
		if(logger.isDebugEnabled())
			logger.debug(restTemplate.getAsyncRequestFactory().getClass().getName());
		HttpHost proxy = new HttpHost(host, port);
		System.setProperty("http.maxConnections", maximumConnections);
		CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClientBuilder.create().useSystemProperties().setProxy(proxy)
				.setSSLHostnameVerifier(new NullHostnameVerifier()).build();
		HttpComponentsAsyncClientHttpRequestFactory clientReqFactory = new HttpComponentsAsyncClientHttpRequestFactory(
				httpAsyncClient);
		restTemplate.setAsyncRequestFactory(clientReqFactory);
	}

	public AsyncRestTemplate getRestTemplate() {
		return restTemplate;
	}

	public class NullHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}*/