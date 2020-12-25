package com.manish.categorization.rest.dto;

public class ExceptionResponse implements GenericResponse {

	String errorMessage;
	public ExceptionResponse() {
	}
	public ExceptionResponse(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
