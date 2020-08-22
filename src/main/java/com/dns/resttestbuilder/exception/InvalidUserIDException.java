package com.dns.resttestbuilder.exception;

public class InvalidUserIDException  extends RuntimeException {

	private static final long serialVersionUID = 749315864365942119L;

	public InvalidUserIDException(Class<?> classType, Long itemID) {
		super("The "+classType.getSimpleName()+", with ID: "+itemID.longValue()+", which you are trying to access belongs to other user");
	}
}