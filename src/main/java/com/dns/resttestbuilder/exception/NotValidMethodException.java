package com.dns.resttestbuilder.exception;

import com.dns.resttestbuilder.entity.Step;

public class NotValidMethodException extends RuntimeException {

	private static final long serialVersionUID = 3672208665523854505L;

	public NotValidMethodException(Step step, String methodName, String extraMessage, String combinations) {
		super("The step nammed: " + step.getName()+ ", kindOf: " + step.getStepKind()+", with order: "+step.getStepOrder()
				+ ", has not valid method format for: " + methodName 
				+ extraMessage+". Method names vs params count " +combinations);
	}
}
