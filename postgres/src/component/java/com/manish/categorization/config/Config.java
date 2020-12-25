package com.manish.categorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.manish.categorization.sdp.config.CaasConfigBean;

@Component
public class Config {

	@Autowired
	private Environment env;
	
	@Autowired
	CaasConfigBean caasConfigBean;
	
	private boolean proxyEnabled;

	private String host;
	
	private int batchSize;

	private Integer port;

	public Environment getEnv() {
		return env;
	}

	public int getBatchSize() {
		return Integer.valueOf(caasConfigBean.getCaas().getBatchSize());
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public boolean isProxyEnabled() {
		return caasConfigBean.getCaas().isProxyEnabled();
	}

	public void setProxyEnabled(boolean proxyEnabled) {
		this.proxyEnabled = proxyEnabled;
	}

	public String getHost() {
		return caasConfigBean.getCaas().getProxyHost();
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return caasConfigBean.getCaas().getProxyPort();
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
