package com.dns.resttestbuilder.controlleradvice;

import java.util.HashMap;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dns.resttestbuilder.exception.MainRequestException;
import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.exception.NotValidInFormatException;
import com.dns.resttestbuilder.exception.NotValidKeyArrayException;
import com.dns.resttestbuilder.exception.NotValidMethodException;
import com.dns.resttestbuilder.exception.NotValidStepOrderException;
import com.dns.resttestbuilder.exception.NotValidatedException;
import com.dns.resttestbuilder.exception.NullPropertyException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestControllerAdvice
public class ControllerAdvice {

	private static final String ERROR_ON_ELEMENT = ": Error on element: ";

	@ResponseBody
	@ExceptionHandler(MainRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorFormat duplicatedMainRequest(MainRequestException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(InvalidUserIDException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	ErrorFormat invalidUserID(InvalidUserIDException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ErrorFormat noFound(NotFoundException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(NotValidInFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorFormat badInFormat(NotValidInFormatException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	HashMap<String, String> badConsrain(ConstraintViolationException ex) {
		HashMap<String, String> errorsMap = new HashMap<>();
		ex.getConstraintViolations().forEach((err) -> {
			String afected = err.getPropertyPath().toString();
			String message = err.getMessage();
			errorsMap.put(afected, message);
		});
		return errorsMap;
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	HashMap<String, String> badArgument(MethodArgumentNotValidException ex) {
		HashMap<String, String> errorsMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach((err) -> {
			String afected = errorsMap.size() + ERROR_ON_ELEMENT + err.getField();
			String message = err.getDefaultMessage();
			errorsMap.put(afected, message);
		});
		return errorsMap;
	}

	@ResponseBody
	@ExceptionHandler(NotValidatedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	HashMap<String, Object> notValidated(NotValidatedException ex) {
		HashMap<String, Object> errorsMap = new HashMap<>();
		ex.getErrors().entrySet().forEach((err) -> {
			BindingResult br = err.getValue();
			br.getFieldErrors().forEach((fe) -> {
				errorsMap.put(errorsMap.size() + ERROR_ON_ELEMENT + err.getKey() + "." + fe.getField(),
						fe.getDefaultMessage());
			});
		});
		errorsMap.put("Affected_JSON", new Gson().fromJson(new Gson().toJson(ex.getCurrent()), Object.class));
		return errorsMap;
	}

	@ResponseBody
	@ExceptionHandler(NotValidKeyArrayException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorFormat badKeyArrayFormat(NotValidKeyArrayException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(NotValidMethodException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorFormat badMethod(NotValidMethodException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(NotValidStepOrderException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorFormat badValidStepOrder(NotValidStepOrderException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(NullPropertyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorFormat nullStepProperty(NullPropertyException ex) {
		return getError(ex);
	}

	@ResponseBody
	@ExceptionHandler(UserIDNotFoundException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	ErrorFormat userIDNotFoundException(UserIDNotFoundException ex) {
		return getError(ex);
	}

	ErrorFormat getError(RuntimeException ex) {
		ErrorFormat error = new ErrorFormat();
		error.setError(ex.getMessage());
		return error;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class ErrorFormat {
		String error;
	}
}
