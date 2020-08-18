package com.dns.resttestbuilder.model.steps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.embedded.EditFieldStepModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component
public class EditFieldStep {

	@Autowired
	ReservedNames reservedNames;

	@Autowired
	DefaultData defaultData;

	private static final int MAX_DNI_NUMBER = 100000000;

	private final static String DNI_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

	private final static String REGEXP_GET_LAST_NUMBER_IN_SEQUENCE = "(\\d+)(?!.*\\d)";

	private final static Random RANDOM = new Random();

	private final static Pattern PATTERN_GET_LAST_NUMBER_IN_SEQUENCE = Pattern
			.compile(REGEXP_GET_LAST_NUMBER_IN_SEQUENCE);

	public final static String DEFAUL_METHOD = "doNothing";

	private HashMap<String, String> playKeyVsLastControlNumber = new HashMap<String, String>();

	public void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		Long stepNumber = step.getStepOrder();
		EditFieldStepModel editFieldStepModel = new Gson().fromJson(step.getStepModel(), EditFieldStepModel.class);
		
		String inFromModel = editFieldStepModel.getInJson();
		Map<String, String> plainKeyFieldsVSMethod = editFieldStepModel.getPlainKeyVsMehtod();
		JsonElement inJSON = defaultData.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,
				inFromModel);
		HashMap<Long, String> inNumberVsINJson = new HashMap<>();
		inNumberVsINJson.put(1L, inJSON.toString());
		stepNumberVSInNumberVSInJSON.put(stepNumber, inNumberVsINJson);
		
		JsonElement outJSON = defaultData.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,
				inFromModel);
		String outJSONString = applyScripts(outJSON, plainKeyFieldsVSMethod);
		stepNumberVSOutJSON.put(stepNumber, outJSONString);
	}

	/**
	 * Metodo para eliminar los datos guardados en esta clase
	 */
	public void clearCache() {
		synchronized (playKeyVsLastControlNumber) {
			playKeyVsLastControlNumber.clear();
		}
	}

	/**
	 * Genera un nuevo numero de dni aleatorio
	 * 
	 * @param plainKey   no se usa
	 * @param valueModel no se usa
	 * @return Nuevo DNI NNNNNNNNL
	 */
	public String getNewDNINumber(String valueModel) {
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
	public String increaseControlNumber(String controlSequence) {
//		String lastSequence = getLastStoredSecuence(plainKey, controlSequence);
		String lastSequence = controlSequence;
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

//	private String getLastStoredSecuence(String plainKey, String controlSequence) {
//		synchronized (playKeyVsLastControlNumber) {
//			if (playKeyVsLastControlNumber.containsKey(plainKey)) {
//				return playKeyVsLastControlNumber.get(plainKey);
//			} else {
//				return controlSequence;
//			}
//		}
//	}

	private char calculateDNILetter(String baseDNI) {
		Integer dniInt = Integer.parseInt(baseDNI);
		int rest = dniInt % 23;
		return DNI_LETTERS.charAt(rest);
	}

	public String applyScripts(JsonElement rootModel, Map<String, String> plainKeyFieldsVSMethod) {
		try {
			iterateOverElements(rootModel, plainKeyFieldsVSMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootModel.toString();
	}

	private void iterateOverElements(JsonElement rootModel, Map<String, String> plainKeyFieldsVSMethod)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (var plainKeyVsMethod : plainKeyFieldsVSMethod.entrySet()) {
			JsonElement children=rootModel;
			String plainKeyField = plainKeyVsMethod.getKey();
			String methodName = plainKeyVsMethod.getValue();
			String[] idElements = plainKeyField.split(reservedNames.getIdentifierSeparator());
			iterateOverElements(idElements, methodName, children);
		}
	}
	
	private void iterateOverElements(String[] idElements, String methodName, JsonElement children) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < idElements.length; i++) {
			String idElement = idElements[i];
			if (defaultData.isOtherItem(idElements, i)) {
				children = defaultData.getNextChildren(children, idElement);
			} else {
				updateValue(methodName, children, idElement);
			}
		}
	}

	private void updateValue(String methodName, JsonElement children, String idElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (children.isJsonObject()) {
			String keyName = idElement.split(reservedNames.getKeyIdentifier())[1];
			updateValueJson(methodName, children, keyName);
		} else {
			String arrayIndex = idElement.split(reservedNames.getArrayIdentifier())[1];
			updateValueJsonAray(methodName, children, arrayIndex);
		}
	}


	public void updateValueJsonAray(String methodName, JsonElement children, String arrayIndex)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Integer idx = Integer.parseInt(arrayIndex);
		JsonArray chArr = children.getAsJsonArray();
		String initialValue = chArr.get(idx).getAsString();
		String updatedValue = initialValue;
		updatedValue = executeSelectedMethod(methodName, initialValue);
		chArr.set(idx, new JsonPrimitive(updatedValue));
	}

	public void updateValueJson(String methodName, JsonElement children, String key)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		JsonObject chJs = children.getAsJsonObject();
		String initialValue = chJs.get(key).getAsString();
		String updatedValue = initialValue;
		updatedValue = executeSelectedMethod(methodName, initialValue);
		chJs.add(key, new JsonPrimitive(updatedValue));
	}

	private String executeSelectedMethod(String methodName, String modelValue)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method mt = getClass().getDeclaredMethod(methodName, String.class);
		return (String) mt.invoke(this, modelValue);
	}
}
