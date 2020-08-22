package com.dns.resttestbuilder.exception;

public class NullPropertyException extends RuntimeException {

	private static final long serialVersionUID = -7026335934023125266L;

	public NullPropertyException(Object object, String propertyName) {
		super("The element: "+object.getClass().getSimpleName()+" needs the property: " + propertyName);
	}

}
