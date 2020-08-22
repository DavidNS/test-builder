package com.dns.resttestbuilder.exception;

import java.util.HashMap;

import org.springframework.validation.BindingResult;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NotValidatedException extends RuntimeException {

	private static final long serialVersionUID = 4622287167825720133L;

	Object current;
	HashMap<String, BindingResult> errors;

	public NotValidatedException(Object current, HashMap<String, BindingResult> errors) {
		this.current = current;
		this.errors = errors;
	}
}
