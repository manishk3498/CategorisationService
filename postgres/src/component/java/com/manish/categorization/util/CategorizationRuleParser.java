package com.manish.categorization.util;


import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMappingException;
import org.springframework.oxm.castor.CastorMarshaller;

public class CategorizationRuleParser {
	private CastorMarshaller unmarshaller;

	public CategorizationRuleParser(){
		this.unmarshaller = new org.springframework.oxm.castor.CastorMarshaller();
		unmarshaller.setMappingLocation(new ClassPathResource("categorization-rule-mappings.xml"));
		try {
			unmarshaller.afterPropertiesSet();
		} catch (CastorMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CategorizationRules unmarshal(String xml) {
		Source src = new StreamSource(new java.io.StringReader(xml));
		CategorizationRules rules = null;
		try {
			rules = (CategorizationRules) unmarshaller.unmarshal(src);
		} catch (XmlMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rules;
	}

}
