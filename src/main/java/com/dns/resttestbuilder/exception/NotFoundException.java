package com.dns.resttestbuilder.exception;

public class NotFoundException  extends RuntimeException {

	private static final long serialVersionUID = -1640920922804553329L;

	public NotFoundException(Class<?> classType, Long id) {
	    super("Could not find "+classType.getSimpleName()+" with id: "+id);
	  }
	}