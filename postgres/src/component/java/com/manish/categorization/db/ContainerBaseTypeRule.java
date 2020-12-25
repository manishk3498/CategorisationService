package com.manish.categorization.db;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.manish.categorization.BaseType;

public class ContainerBaseTypeRule implements Serializable {
	private String container;
	private String baseType;

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getBaseType() {
		return baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseType == null) ? 0 : baseType.hashCode());
		result = prime * result + ((container == null) ? 0 : container.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContainerBaseTypeRule other = (ContainerBaseTypeRule) obj;
		if (baseType == null) {
			if (other.baseType != null)
				return false;
		} else if (!baseType.equals(other.baseType))
			return false;
		if (container == null) {
			if (other.container != null)
				return false;
		} else if (!container.equals(other.container))
			return false;
		return true;
	}

	public boolean isMatching(String container, String baseType) {
		
		if(StringUtils.isEmpty(baseType)){
			//If base type is not present then keyword should be used for categorization
			return Boolean.TRUE;
		}else if(!BaseType.DEBIT.toString().equals(baseType) && !BaseType.CREDIT.toString().equals(baseType)){
			//If base type is not debit/credit and is unknown or something else 
			return Boolean.TRUE;
		}
		return this.container.equals(container) && this.baseType.equals(baseType);
	}

}
