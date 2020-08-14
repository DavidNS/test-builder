package com.dns.resttestbuilder.exception;

public class InvalidUserIDException  extends RuntimeException {

	private static final long serialVersionUID = 749315864365942119L;

	public InvalidUserIDException(Class<?> classType, Long itemID) {
		super("El "+classType.getSimpleName()+" con ID: "+itemID.longValue()+" que intenta acceder/modificar ya pertenece a otro usuario");
	}
}