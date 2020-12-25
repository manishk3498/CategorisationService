package com.manish.categorization.sdp.config;

import com.yodlee.sdp.config.mapping.ConfigProperty;

/**
 * 
 * @author smohanty
 *
 */
public class CaasConfigBean {
	
	@ConfigProperty("CAAS")
	private Caas caas;

	public Caas getCaas() {
		return caas;
	}

	public void setCaas(Caas caas) {
		this.caas = caas;
	}
	
	

}
