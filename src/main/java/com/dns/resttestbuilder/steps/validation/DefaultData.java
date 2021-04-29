package com.dns.resttestbuilder.steps.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;
import com.google.gson.JsonElement;

@Component
public class DefaultData {

	
	@FunctionalInterface
	public interface UdpateElementConsumer {
		void consume(String methodName, JsonElement children, String idElement)
				throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	}

	public void handleCreatingObjectBeforeCreatingUser(Long userID) {
		if (userID == null) {
			throw new UserIDNotFoundException();
		}
	}

	public void handleNotValidUserID(Class<?> itemClass, Long itemID, Long previousUserID, Long currentUserID) throws InvalidUserIDException {
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
		return () -> new NotFoundException(classType, id);
	}

}
