package com.projeto.senac.exception;

public class CriptoExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public CriptoExistsException(String msg) {
		super(msg);
	}
	
}
