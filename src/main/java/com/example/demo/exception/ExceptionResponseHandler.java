package com.example.demo.exception;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorResponse errors = new ErrorResponse();
		errors.setErrorCode(HttpStatus.BAD_REQUEST.value());
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			errors.setErrorMessage(error.getDefaultMessage());
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> nullPointerException(NullPointerException ex, WebRequest requet)
			throws IOException {
		ErrorResponse errors = new ErrorResponse();
		errors.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errors.setErrorMessage("NullPointerException");
		return new ResponseEntity<ErrorResponse>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleFileSizeLimitExceeded(Exception exc, WebRequest request) {
		ErrorResponse errors = new ErrorResponse();
		errors.setErrorCode(HttpStatus.BAD_REQUEST.value());
		String[] s = exc.getMessage().split(";");
		errors.setErrorMessage(exc.getMessage());
		return new ResponseEntity<ErrorResponse>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BookNotFound.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(BookNotFound exc, WebRequest request) {
		ErrorResponse errors = new ErrorResponse();
		errors.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errors.setErrorMessage(exc.getMessage());
		return new ResponseEntity<ErrorResponse>(errors, HttpStatus.BAD_REQUEST);
	}



	@ExceptionHandler(UserAlreadyRegistred.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyRegisteredException(UserAlreadyRegistred exc, WebRequest request) {
		ErrorResponse errors = new ErrorResponse();
		errors.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errors.setErrorMessage("user already registred");
		return new ResponseEntity<ErrorResponse>(errors, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(BookAlreadyRegistered.class)
	public ResponseEntity<ErrorResponse> handleBookAlreadyRegisteredException(BookAlreadyRegistered exc, WebRequest request) {
		ErrorResponse errors = new ErrorResponse();
		errors.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errors.setErrorMessage("book already registred");
		return new ResponseEntity<ErrorResponse>(errors, HttpStatus.BAD_REQUEST);
	}
}
