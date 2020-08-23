package com.dns.resttestbuilder.exception;

import com.dns.resttestbuilder.entity.Step;

public class NotValidKeyArrayException extends RuntimeException {

	private static final long serialVersionUID = -71980676685494866L;

	public NotValidKeyArrayException(Step step, String fullMap, String entry, String extraMessage) {
		super("The step nammed: " + step.getName() + ", kindOf: " + step.getStepKind() +", with order: "+step.getStepOrder()
				+ ", has not valid plainKey format for entry: " + fullMap + " and key: " + entry
				+ extraMessage);
	}
}
