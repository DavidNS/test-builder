package com.dns.resttestbuilder.configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.entity.MappedValue;
import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Component
public class DefaultData {

	@Autowired
	ReservedNames reservedNames;

	@FunctionalInterface
	public interface UdpateElementConsumer {
		void consume(String methodName, JsonElement children, String idElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	}

	public MappedValue handleMappedValue(Long userID, MappedValue mappedValue) {
		handleCreatingObjectBeforeCreatingUser(userID);
		handleNotValidUserID(MappedValue.class, mappedValue.getId(), mappedValue.getUserID(), userID);
		handleNullProperty(mappedValue::getKeyVsValue, HashMap::new, mappedValue::setKeyVsValue);
		return mappedValue;
	}

	public void handleCreatingObjectBeforeCreatingUser(Long userID) {
		if (userID == null) {
			throw new UserIDNotFoundException();
		}
	}

	public void handleNotValidUserID(Class<?> itemClass, Long itemID, Long previousUserID, Long currentUserID) {
		if (itemID != null && previousUserID != null && !previousUserID.equals(currentUserID)) {
			throw new InvalidUserIDException(itemClass, itemID);
		}
	}

	public <T> void handleNotEqualsProperty(Class<T> classType, Supplier<T> previousProp, Supplier<T> newProp) {
		if (!previousProp.get().equals(newProp.get())) {
			// Throw notEqualsException
		}
	}

	public <T> void handleNullProperty(Supplier<T> getProperty, Supplier<T> newProperty, Consumer<T> setProperty) {
		T property = getProperty.get();
		if (property == null) {
			property = newProperty.get();
			setProperty.accept(property);
		}
	}

	public Supplier<? extends NotFoundException> getNotFoundSupplier(Class<?> classType, Long id) {
		return () -> new NotFoundException(classType, id);
	}

	public JsonElement getInputJsonElement(HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, String inJSON) {
		JsonElement jsonElement = null;
		if (inJSON.contains(reservedNames.getInputIdentifier())) {
			String[] identifiers = inJSON.split(reservedNames.getIdentifierSeparator());
			String stepID = identifiers[0].replaceFirst(reservedNames.getStepIdentifier(), "");
			String inID = identifiers[1].replaceFirst(reservedNames.getInputIdentifier(), "");
			jsonElement = JsonParser
					.parseString(stepNumberVSInNumberVSInJSON.get(Long.parseLong(stepID)).get(Long.parseLong(inID)));
		} else if (inJSON.contains(reservedNames.getOutputIdentifier())) {
			String[] identifiers = inJSON.split(reservedNames.getIdentifierSeparator());
			String stepID = identifiers[0].replaceFirst(reservedNames.getStepIdentifier(), "");
			jsonElement = JsonParser.parseString(stepNumberVSOutJSON.get(Long.parseLong(stepID)));
		} else {
			jsonElement = JsonParser.parseString(inJSON);
		}
		return jsonElement;
	}

	public JsonElement getNextChildren(JsonElement children, String idElement) {
		if (children.isJsonArray()) {
			String arrayIndex = idElement.split(reservedNames.getArrayIdentifier())[1];
			children = children.getAsJsonArray().get(Integer.parseInt(arrayIndex)).getAsJsonArray();
		} else if(children.isJsonObject()) {
			String keyName = idElement.split(reservedNames.getKeyIdentifier())[1];
			children = children.getAsJsonObject().getAsJsonObject(keyName);
		}
		return children;
	}
	
	public JsonElement getLastChildren(JsonElement children, String idElement) {
		if (children.isJsonArray()) {
			String arrayIndex = idElement.split(reservedNames.getArrayIdentifier())[1];
			children = children.getAsJsonArray().get(Integer.parseInt(arrayIndex)).getAsJsonPrimitive();
		} else if(children.isJsonObject()) {
			String keyName = idElement.split(reservedNames.getKeyIdentifier())[1];
			children = children.getAsJsonObject().get(keyName).getAsJsonPrimitive();
		}
		return children;
	}
	
	public boolean isOtherItem(String[] elements, int i) {
		return i != elements.length - 1;
	}
}
