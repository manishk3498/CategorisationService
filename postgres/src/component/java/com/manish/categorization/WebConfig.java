package com.manish.categorization;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.manish.categorization.algo.legacy.Search;
import com.manish.categorization.config.Config;
import com.manish.categorization.db.TransactionCategoryCache;
import com.manish.categorization.sdp.config.CaasConfigBean;
import com.yodlee.sdp.config.mapping.ConfigMappingFactory;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	

/*	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}*/

	@Bean
	AsyncRestTemplate asyncRestTemplate() {
		return new AsyncRestTemplate();
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	Config config() {
		return new Config();
	}

	@Bean
	TransactionCategoryCache transactionCategoryCache() {
		return new TransactionCategoryCache();
	}

	@Bean
	Search search() {
		return Search.getInstance();
	}	
	  
    @Bean
	public CaasConfigBean getCaasConfigBean() {	
		CaasConfigBean caasConfigBean = ConfigMappingFactory.loadConfiguration(System.getProperty("com.yodlee.sdp.configJsonPath"),
				"caas", CaasConfigBean.class);
		return caasConfigBean;
	}

}