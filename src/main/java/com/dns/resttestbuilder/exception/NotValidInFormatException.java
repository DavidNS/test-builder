package com.dns.resttestbuilder.exception;

import com.dns.resttestbuilder.entity.Step;

public class NotValidInFormatException extends RuntimeException {


	private static final long serialVersionUID = -609012410136955902L;

	public NotValidInFormatException(Step step, String inJson, String extraMessage) {
		super("The step nammed: " + step.getName() + ", kindOf: " + step.getStepKind()+", with order: "+step.getStepOrder()
				+ ", has not valid format, " + extraMessage + ", but has: " + inJson);
	}
}