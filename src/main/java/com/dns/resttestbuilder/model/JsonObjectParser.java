package com.dns.resttestbuilder.model;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.exception.NotValidJSONFormatException;
import com.google.gson.Gson;

@Component
public class JsonObjectParser {

	public <T> T objectToModel(Object o, Class<T> castClass, Consumer<T> setter) {
		if (castClass.isInstance(o)) {
			return castClass.cast(o);
		}
		try {
			T obj =new Gson().fromJson(new Gson().toJson(o), castClass);
			setter.accept(obj);
			return obj;
		} catch (Exception e) {
			throw new NotValidJSONFormatException(o, castClass);
		}
	}

}
