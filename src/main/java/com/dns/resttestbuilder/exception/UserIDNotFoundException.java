package com.dns.resttestbuilder.exception;

public class UserIDNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 749315864365942119L;

	public UserIDNotFoundException() {
		super("Trying to create an object without user-id. Must send a valid user-id to be allowed to create an object. If do not have an user-id, shall create the user first");
	}
}
