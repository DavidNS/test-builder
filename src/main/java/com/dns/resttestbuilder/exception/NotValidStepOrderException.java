package com.dns.resttestbuilder.exception;

import com.dns.resttestbuilder.steps.Step;

public class NotValidStepOrderException extends RuntimeException {

	private static final long serialVersionUID = 875146302344323878L;

	public NotValidStepOrderException(Step step, String expected, String received) {
		super("Not valid step order of step with name: " + step.getName()+ ", kindOf: " + step.getStepKind()   + ", expected: "
				+ expected + ", received: " + received);
	}

}
