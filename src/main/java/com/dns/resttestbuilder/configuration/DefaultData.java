package com.dns.resttestbuilder.configuration;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.entity.MappedValue;
import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class DefaultData {
	
	@Autowired
	ReservedNames reservedNames;

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
	
	public JsonObject getInputJson(HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, String inJSON) {
		JsonObject jsonObject = null;
		if (inJSON.contains(reservedNames.getInputIdentifier())) {
			String[] identifiers = inJSON.split(reservedNames.getInOutSeparator());
			String stepID = identifiers[1];
			String inID = identifiers[3];
			jsonObject = JsonParser
					.parseString(stepNumberVSInNumberVSInJSON.get(Long.parseLong(stepID)).get(Long.parseLong(inID)))
					.getAsJsonObject();
		} else if (inJSON.contains(reservedNames.getOutputIdentifier())) {
			String[] identifiers = inJSON.split(reservedNames.getInOutSeparator());
			String stepID = identifiers[1];
			jsonObject = JsonParser.parseString(stepNumberVSOutJSON.get(Long.parseLong(stepID))).getAsJsonObject();
		} else {
			jsonObject = JsonParser.parseString(inJSON).getAsJsonObject();
		}
		return jsonObject;
	}
}
