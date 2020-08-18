package com.dns.resttestbuilder.model.steps;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditScripts {
	

	private static final int MAX_DNI_NUMBER = 100000000;

	private final static String DNI_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

	private final static String REGEXP_GET_LAST_NUMBER_IN_SEQUENCE = "(\\d+)(?!.*\\d)";

	private final static Random RANDOM = new Random();

	private final static Pattern PATTERN_GET_LAST_NUMBER_IN_SEQUENCE = Pattern
			.compile(REGEXP_GET_LAST_NUMBER_IN_SEQUENCE);
	
	public String getNewDNINumber(String valueModel) {
		String newDNINumber = String.valueOf(generateRandomIntIntRange(0, MAX_DNI_NUMBER));
		return newDNINumber + calculateDNILetter(newDNINumber);
	}

	
	private int generateRandomIntIntRange(int min, int max) {
		return RANDOM.nextInt((max - min) + 1) + min;
	}
	
	public String newRandomLastNumberInSequence(String controlSequence, String minRange, String maxRange) {
		Matcher matcher = PATTERN_GET_LAST_NUMBER_IN_SEQUENCE.matcher(controlSequence);
		if (matcher.find()) {
			int startRange = matcher.start();
			int endRange = matcher.end();
			Integer lastNumberFound=Integer.parseInt(controlSequence.substring(startRange, endRange));
			String newRandomSequence = String.valueOf(generateRandomIntIntRange(lastNumberFound+Integer.parseInt(minRange),lastNumberFound+Integer.parseInt( maxRange)));
			controlSequence=newRandomSequence;
		} else {
			controlSequence = controlSequence + "1";

		}
		return controlSequence;
	}

	private char calculateDNILetter(String baseDNI) {
		Integer dniInt = Integer.parseInt(baseDNI);
		int rest = dniInt % 23;
		return DNI_LETTERS.charAt(rest);
	}

}
