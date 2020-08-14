package com.dns.resttestbuilder.exception;

public class UserIDNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 749315864365942119L;

	public UserIDNotFoundException() {
		super("Se ha intentado crear un objeto sin antes haber creado sin identificador de usuario.\nDebe enviar un identificador de usuario.");
	}
}
