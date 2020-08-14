package com.dns.resttestbuilder.model;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class EditFieldScripts {

	private static final int MAX_DNI_NUMBER = 100000000;

	private final static String DNI_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

	private final static String REGEXP_GET_LAST_NUMBER_IN_SEQUENCE = "(\\d+)(?!.*\\d)";

	private final static Random RANDOM = new Random();

	private final static Pattern PATTERN_GET_LAST_NUMBER_IN_SEQUENCE = Pattern
			.compile(REGEXP_GET_LAST_NUMBER_IN_SEQUENCE);

	public final static String DEFAUL_METHOD = "doNothing";

	private HashMap<String, String> playKeyVsLastControlNumber = new HashMap<String, String>();

	
	/**
	 * Metodo para eliminar los datos guardados en esta clase
	 */
	public void clearCache() {
		synchronized (playKeyVsLastControlNumber) {
			playKeyVsLastControlNumber.clear();
		}
	}
	
	
	/**
	 * Funcion por defecto. No hace nada. Se ignora si aparece en la ejecucion. Pero
	 * debe mantener la concordancia con el campo DEFAUL_METHOD
	 * 
	 * @param s
	 * @return
	 */
	public String doNothing(String s, String s2) {
		return s2;
	}

	/**
	 * Genera un nuevo numero de dni aleatorio
	 * 
	 * @param plainKey   no se usa
	 * @param valueModel no se usa
	 * @return Nuevo DNI NNNNNNNNL
	 */
	public String getNewDNINumber(String plainKey, String valueModel) {
		String newDNINumber = String.valueOf(generateRandomIntIntRange(0, MAX_DNI_NUMBER));
		return newDNINumber + calculateDNILetter(newDNINumber);
	}

	/**
	 * Genera un numero aleatorio entre
	 * 
	 * @param min rangoMin(incl)
	 * @param max rangoMax(excl)
	 * @return numeroAleatorio
	 */
	private int generateRandomIntIntRange(int min, int max) {
		return RANDOM.nextInt((max - min) + 1) + min;
	}

	/**
	 * Incrementa el ultimo numero que encuente en una secuencia o anyade uno
	 * 
	 * @param plainKey        clave usada a lo largo del programa para recordar la
	 *                        ultima secuencia para esa clave
	 * @param controlSequence numero de secuencia que se usara como patron
	 * @return PATRON_NUMEROS_PATRON_ULTIMONUMEROINCREMENTADO_PATRON o
	 *         PATRONSINNUMERO_ANYADEUN1ALACADENA
	 */
	public String increaseControlNumber(String plainKey, String controlSequence) {
		String lastSequence = getLastStoredSecuence(plainKey, controlSequence);
		Matcher matcher = PATTERN_GET_LAST_NUMBER_IN_SEQUENCE.matcher(lastSequence);
		if (matcher.find()) {
			int startRange = matcher.start();
			int endRange = matcher.end();
			String lastIntegerIncreased = String
					.valueOf(1 + Integer.parseInt(lastSequence.substring(startRange, endRange)));
			lastSequence = lastSequence.substring(0, startRange) + lastIntegerIncreased
					+ lastSequence.substring(endRange, lastSequence.length());

		} else {
			lastSequence = lastSequence + "1";

		}
		return lastSequence;
	}

	private String getLastStoredSecuence(String plainKey, String controlSequence) {
		synchronized (playKeyVsLastControlNumber) {
			if (playKeyVsLastControlNumber.containsKey(plainKey)) {
				return playKeyVsLastControlNumber.get(plainKey);
			} else {
				return controlSequence;
			}
		}
	}

	private char calculateDNILetter(String baseDNI) {
		Integer dniInt = Integer.parseInt(baseDNI);
		int rest = dniInt % 23;
		return DNI_LETTERS.charAt(rest);
	}

//	public JSONObject applyScripts(JSONObject rootModel, ObservableList<PreRequestElement> preRequestScriptList) {
//		try {
//			iterateOverPreRequestElement(rootModel, preRequestScriptList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return rootModel;
//
//	}

	/**
	 * Metodo para iterar sobre los campos de un json valido que se usara como
	 * modelo a la hora de aplicar las funciones de prerequest
	 * 
	 * @param rootModel            JsonValido
	 * @param preRequestScriptList Lista generica con los campos a iterar
	 * @throws NoSuchMethodException     TODO
	 * @throws IllegalAccessException    TODO
	 * @throws InvocationTargetException TODO
	 */
//	private void iterateOverPreRequestElement(JSONObject rootModel, List<PreRequestElement> preRequestScriptList)
//			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//		for (PreRequestElement preRequestElement : preRequestScriptList) {
//			String methodName = preRequestElement.getMethod();
//			if (methodName.equals(DEFAUL_METHOD)) {
//				continue;
//			}
//
//			JSONObject children = rootModel;
//			String plainKeyField = preRequestElement.getPlainKeyField();
//			String[] elements = plainKeyField.split(JSONModelTreeBuilder.KEY_SEPPARATTOR);
//
//			children = iterateOverElements(methodName, children, plainKeyField, elements);
//		}
//	}
//
//	private JSONObject iterateOverElements(String methodName, JSONObject children, String plainKeyField,
//			String[] elements) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//		for (int i = 1; i < elements.length; i++) {
//			String key = elements[i];
//			String[] arrayElements = key.split(JSONModelTreeBuilder.ARRAY_SEPARATTOR);
//			if (isAnArray(arrayElements)) {
//				String arrayName = arrayElements[0];
//				int arrayIndex = Integer.parseInt(arrayElements[1]);
//				if (isOtherItem(elements, i)) {
//					// Case nested json inside array
//					children = children.getJSONArray(arrayName).getJSONObject(arrayIndex);
//				} else {
//					// Case last item but array
//					String modelValue = children.getJSONArray(arrayName).getString(arrayIndex);
//					String modelValueAfterScript = executeSelectedMethod(methodName, plainKeyField, modelValue);
//					children.getJSONArray(arrayName).put(arrayIndex, modelValueAfterScript);
//				}
//			} else if (isOtherItem(elements, i)) {
//				// Case nested json
//				children = children.getJSONObject(key);
//			} else {
//				// Case last item
//				String modelValue = children.getString(key);
//				String modelValueAfterScript = executeSelectedMethod(methodName, plainKeyField, modelValue);
//				children.put(key, modelValueAfterScript);
//			}
//
//		}
//		return children;
//	}
//
//	private boolean isOtherItem(String[] elements, int i) {
//		return i != elements.length - 1;
//	}
//
//	private boolean isAnArray(String[] arrayElements) {
//		return arrayElements.length == 2;
//	}
//
//	private String executeSelectedMethod(String methodName, String plainKeyField, String modelValue)
//			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//		Method mt = getClass().getDeclaredMethod(methodName,plainKeyField.getClass(), modelValue.getClass());
//		return (String) mt.invoke(this, plainKeyField, modelValue);
//	}

}
