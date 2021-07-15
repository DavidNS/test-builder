package com.dns.resttestbuilder.exception;

public class NotFoundException  extends RuntimeException {

	private static final long serialVersionUID = -1640920922804553329L;

	public NotFoundException(Class<?> classType, String id) {
	    super("Not found "+classType.getSimpleName()+" with id: "+id);
	  }
	}