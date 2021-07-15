package com.dns.resttestbuilder.testexecutions.execution.steps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.EditFieldStepModel;
import com.dns.resttestbuilder.testexecutions.JsonObjectParser;
import com.dns.resttestbuilder.testexecutions.ReservedNamesParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component
public class EditFieldStep {

	@Autowired
	ReservedNamesParser reservedNamesParser;
	
	@Autowired
	JsonObjectParser jsonObjectParser;
	
	@Autowired
	EditScripts editScripts;

	public void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		Long stepNumber = step.getStepOrder();
		EditFieldStepModel editFieldStepModel =  jsonObjectParser.objectToModel(step.getStepModel(), EditFieldStepModel.class,step::setStepModel) ;
		String inFromModel = editFieldStepModel.getInJson();
		Map<String, String> plainKeyFieldsVSMethod = editFieldStepModel.getPlainKeyVsMehtod();
		JsonElement inJSON = reservedNamesParser.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,
				inFromModel);
		HashMap<Long, String> inNumberVsINJson = new HashMap<>();
		inNumberVsINJson.put(1L, inJSON.toString());
		stepNumberVSInNumberVSInJSON.put(stepNumber, inNumberVsINJson);
		
		JsonElement outJSON = reservedNamesParser.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,
				inFromModel);
		String outJSONString = applyScripts(outJSON, plainKeyFieldsVSMethod);
		stepNumberVSOutJSON.put(stepNumber, outJSONString);
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
			String[] idElements = plainKeyField.split(ReservedNames.IDENTIFIER_SEPARATOR);
			iterateOverElements(idElements, methodName, children);
		}
	}
	
	private void iterateOverElements(String[] idElements, String methodName, JsonElement children) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < idElements.length; i++) {
			String idElement = idElements[i];
			if (reservedNamesParser.isOtherItem(idElements, i)) {
				children = reservedNamesParser.getNextChildren(children, idElement);
			} else {
				updateValue(methodName, children, idElement);
			}
		}
	}

	private void updateValue(String methodName, JsonElement children, String idElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (children.isJsonObject()) {
			updateValueJson(methodName, children, idElement);
		} else {
			String arrayIndex = idElement.substring(idElement.indexOf(ReservedNames.ARRAY_BEGIN_IDENTIFIER,
					idElement.indexOf(ReservedNames.ARRAY_END_IDENTIFIER)));
			updateValueJsonAray(methodName, children, arrayIndex);
		}
	}


	public void updateValueJsonAray(String method, JsonElement children, String arrayIndex)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Integer idx = Integer.parseInt(arrayIndex);
		JsonArray chArr = children.getAsJsonArray();
		String initialValue = chArr.get(idx).getAsString();
		String updatedValue = executeSelectedMethod(method, initialValue);
		chArr.set(idx, new JsonPrimitive(updatedValue));
	}

	public void updateValueJson(String methodName, JsonElement children, String key)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		JsonObject chJs = children.getAsJsonObject();
		String initialValue = chJs.get(key).getAsString();
		String updatedValue = executeSelectedMethod(methodName, initialValue);
		chJs.add(key, new JsonPrimitive(updatedValue));
	}

	private String executeSelectedMethod(String method,String modelValue)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		ArrayList<String> methodParams=new ArrayList<>();
		ArrayList<Class<String>> cl=new ArrayList<>();
		String[] methodIDs=method.split(ReservedNames.IDENTIFIER_SEPARATOR);
		String methodName=methodIDs[0];
		methodParams.add(modelValue);
		cl.add(String.class);
		for (int i = 1; i < methodIDs.length; i++) {
			methodParams.add(methodIDs[i]);
			cl.add(String.class);
		}
		Class<?>[] clss=cl.toArray(Class[]::new);
		Object[] mp= methodParams.toArray(String[]::new);
		return invoke(methodName,clss, mp);
	}

	private String invoke(String methodName, Class<?>[] clss, Object... mp)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method mt = editScripts.getClass().getDeclaredMethod(methodName, clss);
		return (String) mt.invoke(editScripts, mp);
	}
}
