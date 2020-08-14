package com.dns.resttestbuilder.configuration;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.entity.MappedValue;
import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;

@Component
public class DefaultData {

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
}
