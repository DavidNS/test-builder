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
		JsonObject inJSON = defaultData.getInputJson(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, inFromModel);
		JsonObject outJSON = defaultData.getInputJson(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, inFromModel);
		outJSON = applyScripts(outJSON, plainKeyFieldsVSMethod);
		HashMap<Long, String> inNumberVsINJson = new HashMap<>();
		inNumberVsINJson.put(1L, inJSON.toString());
		stepNumberVSInNumberVSInJSON.put(stepNumber, inNumberVsINJson);
		stepNumberVSOutJSON.put(stepNumber, outJSON.toString());
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

	public JsonObject applyScripts(JsonObject rootModel, Map<String, String> plainKeyFieldsVSMethod) {
		try {
			iterateOverPreRequestElement(rootModel, plainKeyFieldsVSMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootModel;
	}

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
	private void iterateOverPreRequestElement(JsonObject rootModel, Map<String, String> plainKeyFieldsVSMethod)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (var plainKeyVsMethod : plainKeyFieldsVSMethod.entrySet()) {
			String plainKeyField = plainKeyVsMethod.getKey();
			String methodName = plainKeyVsMethod.getValue();
			JsonObject children = rootModel;
			String[] elements = plainKeyField.split(reservedNames.getKeyIdentifier());

			children = iterateOverElements(methodName, children, plainKeyField, elements);
		}
	}

	private JsonObject iterateOverElements(String methodName, JsonObject children, String plainKeyField,
			String[] elements) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (int i = 1; i < elements.length; i++) {
			String key = elements[i];
			String[] arrayElements = key.split(reservedNames.getArrayIdentifier());
			if (isAnArray(arrayElements)) {
				String arrayName = arrayElements[0];
				int arrayIndex = Integer.parseInt(arrayElements[1]);
				if (isOtherItem(elements, i)) {
					// Case nested json inside array
					children = children.getAsJsonArray(arrayName).get(arrayIndex).getAsJsonObject();
				} else {
					// Case last item but array
					String modelValue = children.getAsJsonArray(arrayName).get(arrayIndex).getAsString();
					String modelValueAfterScript = executeSelectedMethod(methodName, plainKeyField, modelValue);
					children.getAsJsonArray(arrayName).set(arrayIndex, new JsonPrimitive(modelValueAfterScript));
				}
			} else if (isOtherItem(elements, i)) {
				// Case nested json
				children = children.get(key).getAsJsonObject();
			} else {
				// Case last item
				String modelValue = children.get(key).getAsString();
				String modelValueAfterScript = executeSelectedMethod(methodName, plainKeyField, modelValue);
				children.addProperty(key, modelValueAfterScript);
			}

		}
		return children;
	}

	private boolean isOtherItem(String[] elements, int i) {
		return i != elements.length - 1;
	}

	private boolean isAnArray(String[] arrayElements) {
		return arrayElements.length == 2;
	}

	private String executeSelectedMethod(String methodName, String plainKeyField, String modelValue)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method mt = getClass().getDeclaredMethod(methodName, plainKeyField.getClass(), modelValue.getClass());
		return (String) mt.invoke(this, plainKeyField, modelValue);
	}
}
