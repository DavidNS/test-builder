package com.dns.resttestbuilder.model;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.controller.dto.StepRest;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.exception.NotValidJSONFormatException;
import com.google.gson.Gson;

@Component
public class JsonStepParser {

	public <T> StepRest<T> dbObjectToModel(Long userID, Step db, Class<T> classCast) {
		try {
			String stringJSON = new Gson().toJson(db.getStepModel());
			T stepModel = new Gson().fromJson(stringJSON, classCast);
			return new StepRest<>(db.getId(), userID, db.getName(), db.getStepOrder(), db.getStepKind(), stepModel);
		} catch (Exception e) {
			throw new NotValidJSONFormatException(db.getStepModel(), classCast);
		}
	}



	public <T> T dbObjectToModel(Object o, Class<T> castClass, Consumer<T> setter) {
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
