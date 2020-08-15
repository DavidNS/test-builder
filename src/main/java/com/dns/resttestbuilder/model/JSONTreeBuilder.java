package com.dns.resttestbuilder.model;

import org.springframework.stereotype.Component;

/**
 * Clase encargada de analizar el modelo que se escribe y actualizar los elementos
 * de prerequest necesarios en caso de que sea un json valido
 * 
 * Un mapeador JSON -> Elemento en una lista ClavePlana-Valor
 * @author David Nowakowski Stachyra
 *
 */
@Component
public class JSONTreeBuilder {

	public static final String ARRAY_SEPARATTOR = "___ARRAY___";
	public static final String KEY_SEPPARATTOR = "___KEY___";

//	public JSONObject getPlaneKeyVsElement(String entry, List<PreRequestElement> preRequestScripts) {
//		JSONObject validJSON = null;
//		try {
//			validJSON = new JSONObject(entry);
//			preRequestScripts.clear();
//			getPlaneKeyVsElement(validJSON, "", preRequestScripts);
//		} catch (Exception e) {
//			validJSON = null;
//		}
//
//		return validJSON;
//	}
//
//	private void getPlaneKeyVsElement(JSONObject jsonObject, String previousKey,
//			List<PreRequestElement> preRequestScripts) {
//		for (var key : jsonObject.keySet()) {
//			String updatedKey = previousKey + KEY_SEPPARATTOR + key;
//			JSONObject nestedObject = tryGet(key, jsonObject::getJSONObject);
//			JSONArray nestedArray = tryGet(key, jsonObject::getJSONArray);
//			if (nestedObject != null) {
//				getPlaneKeyVsElement(nestedObject, updatedKey, preRequestScripts);
//			} else if (nestedArray != null) {
//				getPlaneKeyVsElement(nestedArray, updatedKey, preRequestScripts);
//			} else {
//				preRequestScripts.add(new PreRequestElement(updatedKey, PreRequestScripts.DEFAUL_METHOD));
//			}
//
//		}
//	}
//
//	private void getPlaneKeyVsElement(JSONArray jsonArray, String parentKey,
//			List<PreRequestElement> preRequestScripts) {
//		for (int i = 0; i < jsonArray.length(); i++) {
//			String updatedParentKey = parentKey + ARRAY_SEPARATTOR + i;
//			JSONObject nestedObject = tryGet(i, jsonArray::getJSONObject);
//			JSONArray nestedArray = tryGet(i, jsonArray::getJSONArray);
//			if (nestedObject != null) {
//				getPlaneKeyVsElement(nestedObject, updatedParentKey, preRequestScripts);
//			} else if (nestedArray != null) {
//				getPlaneKeyVsElement(nestedArray, updatedParentKey, preRequestScripts);
//			} else {
//				preRequestScripts.add(new PreRequestElement(updatedParentKey, PreRequestScripts.DEFAUL_METHOD));
//			}
//		}
//	}
//
//	private <T, E> T tryGet(E key, Function<E, T> getFunction) {
//		try {
//			return getFunction.apply(key);
//		} catch (Exception e) {
//			return null;
//		}
//	}
}
