
package com.manish.categorization.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.fluent.Executor;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manish.categorization.sdp.config.CaasConfigBean;
/**
 * 
 * @author mgarg
 *
 */
@Component
public class FluentExecutor {

	@Autowired
	CaasConfigBean caasConfigBean;
	
	private static Logger logger = LogManager.getLogger(FluentExecutor.class);
	
	PoolingHttpClientConnectionManager CONNMGR = null;
	HttpClient CLIENT = null;
	private static Executor executor;
	
	@PostConstruct
	public void init(){

		LayeredConnectionSocketFactory ssl = null;
		try {
			ssl = new SSLConnectionSocketFactory(
					(javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault(),
					split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")),
					new NullHostnameVerifier());
		} catch (final SSLInitializationException ex) {
			final SSLContext sslcontext;
			try {
				sslcontext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
				sslcontext.init(null, null, null);
				ssl = new SSLConnectionSocketFactory(sslcontext);

			} catch (final SecurityException ignore) {
			} catch (final KeyManagementException ignore) {
			} catch (final NoSuchAlgorithmException ignore) {
			}
		}
		final Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", ssl != null ? ssl : SSLConnectionSocketFactory.getSocketFactory()).build();
		CONNMGR = new PoolingHttpClientConnectionManager(sfr);
		CONNMGR.setDefaultMaxPerRoute(caasConfigBean.getCaas().getMaximumConnectionsPerRoute());
		CONNMGR.setMaxTotal(caasConfigBean.getCaas().getMaxConnections());
		CONNMGR.setValidateAfterInactivity(caasConfigBean.getCaas().getInactivityTimeInms());
		CLIENT = HttpClientBuilder.create().disableCookieManagement().setConnectionManager(CONNMGR).setRetryHandler(new HttpRequestRetryHandler() {
		    @Override
		    public boolean retryRequest(IOException exception, int executionCount, 
		                                HttpContext context) {
		        if (executionCount > 3) {
		        	if(logger.isInfoEnabled())
		        		logger.info("Maximum tries reached for client http pool "+executionCount);
		            return false;
		        }
		        if (exception instanceof org.apache.http.NoHttpResponseException) {
		        	if(logger.isInfoEnabled())
		        		logger.info("No response from server on " + executionCount + " call" , exception);
		            return true;
		        }
		        return false;
		      }
		   }).build();
		executor = Executor.newInstance(CLIENT);
	
	}

	public static Executor getExecutor() {
		return executor;
	}

	private static String[] split(final String s) {
		if (TextUtils.isBlank(s)) {
			return null;
		}
		return s.split(" *, *");
	}
	public static class NullHostnameVerifier implements HostnameVerifier {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}	
}
