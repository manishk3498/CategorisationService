package com.manish.categorization.http;
/*

package com.manish.categorization.http;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public final class RestProxyTemplate {

	private static final Logger logger = LogManager.getLogger(RestProxyTemplate.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${proxy.enabled}")
	boolean proxyEnabled;

	@Value("${proxy.host}")
	String host;

	@Value("${proxy.port}")
	Integer port;
	
	@Value("${maximum.connections}")
	String maximumConnections;
	
	@PostConstruct
	public void init() {
		if(logger.isDebugEnabled())
			logger.debug(restTemplate.getRequestFactory().getClass().getName());
		
		HttpHost proxy = null;
		if (proxyEnabled) {
			proxy = new HttpHost(host, port);
		}
		System.setProperty("http.maxConnections", maximumConnections);
		CloseableHttpClient httpAsyncClient = HttpClientBuilder.create().useSystemProperties().setProxy(proxy)
				.setSSLHostnameVerifier(new NullHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory clientReqFactory = new HttpComponentsClientHttpRequestFactory(
				httpAsyncClient);
		restTemplate.setRequestFactory(clientReqFactory);
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public class NullHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}*/