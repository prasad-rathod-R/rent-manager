package com.app.rent_manager.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.app.rent_manager.ResponseBody.ResponseHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidRoomException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleInvalidRoomException(InvalidRoomException ex) {
		return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> noSuchelemt(Exception ex) {
		return ResponseHandler.generateResponse("NO DATA PRESENT IN DB", HttpStatus.INTERNAL_SERVER_ERROR, false);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
		return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
	}
}
