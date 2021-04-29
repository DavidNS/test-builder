package com.dns.resttestbuilder.steps.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.dns.resttestbuilder.exception.NotValidatedException;
import com.google.gson.internal.LinkedTreeMap;

@Component
public class GenericValidator {

	@Autowired
	Validator validator;
	
	public <T> void validateObject(T o) throws RuntimeException, IllegalAccessException {
		HashMap<String, BindingResult> keyVSerrors = new HashMap<>();
		BindingResult errors = new BeanPropertyBindingResult(o, o.getClass().getName());
		validator.validate(o, errors);
		addErrors(o.getClass().getSimpleName(), keyVSerrors, errors);
		validateChildObject(o, keyVSerrors, o.getClass().getSimpleName());
		if (keyVSerrors.size() > 0) {
			throw new NotValidatedException(o, keyVSerrors);
		}
	}

	private <T> void validateChildObject(T o, HashMap<String, BindingResult> keyVSerrors, String simpleName)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean prevState = field.canAccess(o);
			field.setAccessible(true);
			Object child = field.get(o);
			if (haveChildren(child)) {
				String complexName = simpleName + "." + child.getClass().getSimpleName();
				BindingResult errors = new BeanPropertyBindingResult(child, child.getClass().getName());
				validator.validate(child, errors);
				addErrors(complexName, keyVSerrors, errors);
				validateChildObject(child, keyVSerrors, complexName);
			}
			field.setAccessible(prevState);
		}

	}

	private boolean haveChildren(Object child) {
		return child != null && !BeanUtils.isSimpleValueType(child.getClass())
				&& !child.getClass().equals(ArrayList.class) && !child.getClass().equals(LinkedTreeMap.class);
	}

	private <T> void addErrors(String key, HashMap<String, BindingResult> keyVSerrors, BindingResult errors) {
		if (errors.hasErrors()) {
			keyVSerrors.put(key, errors);
		}
	}

}
