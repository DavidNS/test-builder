package com.dns.resttestbuilder.configuration;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;

@Component
public class Validation {

	public void handleCreatingObjectBeforeCreatingUser(String userID) {
		if (userID == null) {
			throw new UserIDNotFoundException();
		}
	}

	public void handleNotValidUserID(Class<?> itemClass, Long itemID, String previousUserID, String currentUserID) throws InvalidUserIDException {
		if (itemID != null && previousUserID != null && !previousUserID.equals(currentUserID)) {
			throw new InvalidUserIDException(itemClass, itemID);
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
		return getNotFoundSupplier(classType, String.valueOf(id));
	}
	
	public Supplier<? extends NotFoundException> getNotFoundSupplier(Class<?> classType, String id) {
		return () -> new NotFoundException(classType, id);
	}

}
