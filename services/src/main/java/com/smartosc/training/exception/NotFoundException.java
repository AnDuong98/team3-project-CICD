package com.smartosc.training.exception;

import java.util.function.Supplier;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = -6262094929329166029L;
	
	public NotFoundException(final String message) {
		super(message);
	}
	
	public static Supplier<NotFoundException> supplier(String clazz,Long id){
		return () -> new NotFoundException(String.format("'%s' with id '%d' not found",clazz,id));
	}
	
	public static Supplier<NotFoundException> supplier(String clazz){
		return () -> new NotFoundException(String.format("'%s' not found",clazz));
	}


}
