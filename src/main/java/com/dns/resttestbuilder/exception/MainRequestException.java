package com.dns.resttestbuilder.exception;

import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.StepKind;

public class MainRequestException extends RuntimeException {

	private static final long serialVersionUID = 3833527377157452515L;

	public MainRequestException() {
		super("One, and only one, Step kind of: " + StepKind.SEND_MAIN_REQUEST
				+ ", is mandatory. It also shall have the last order number");
	}

	public MainRequestException(Step... one) {
		super("Detected duplicated kinds: " + StepKind.SEND_MAIN_REQUEST + ", conflict steps: "+writeConflicts(one));
	}

	private static String writeConflicts(Step[] one) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(System.lineSeparator());
		for (Step step : one) {
			stringBuilder.append(" with name: " );
			stringBuilder.append(step.getName());
			stringBuilder.append(" with values: ");
			stringBuilder.append(step.toString());
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

}
