package com.dns.resttestbuilder.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JSONTreeIterator {


	public static final String ARRAY_SEPARATTOR = "___ARRAY___";
	public static final String KEY_SEPPARATTOR = "___KEY___";
	
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
//			String[] elements = plainKeyField.split(KEY_SEPPARATTOR);
//
//			children = iterateOverElements(methodName, children, plainKeyField, elements);
//		}
//	}
//
//	private JSONObject iterateOverElements(String methodName, JSONObject children, String plainKeyField,
//			String[] elements) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//		for (int i = 1; i < elements.length; i++) {
//			String key = elements[i];
//			String[] arrayElements = key.split(ARRAY_SEPARATTOR);
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
