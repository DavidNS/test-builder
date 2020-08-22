package com.dns.resttestbuilder.exception;

public class NotValidJSONFormatException extends RuntimeException {


	private static final long serialVersionUID = 3053124344370797848L;
	
	public NotValidJSONFormatException(String s) {
		super("Unnable to transform from generic object to concrete item: "+s);
	}
	
	public NotValidJSONFormatException(Object o, Class<?> classType) {
		super("Unnable to transform from generic object to concrete item: "+classType.getSimpleName()+ ", with the parameters: "+o.toString());
	}
}